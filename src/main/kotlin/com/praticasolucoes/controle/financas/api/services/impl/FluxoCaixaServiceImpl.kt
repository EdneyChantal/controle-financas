package com.praticasolucoes.controle.financas.api.services.impl

import com.praticasolucoes.controle.financas.api.domain.fluxocaixa.ElementoFluxo
import com.praticasolucoes.controle.financas.api.domain.fluxocaixa.IdLinhaFluxo
import com.praticasolucoes.controle.financas.api.domain.fluxocaixa.TableDados
import com.praticasolucoes.controle.financas.api.domain.matrix.RelatorioDTO
import com.praticasolucoes.controle.financas.api.services.AccountBankService
import com.praticasolucoes.controle.financas.api.services.FluxoCaixaService
import com.praticasolucoes.controle.financas.api.services.MovementBankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class FluxoCaixaServiceImpl(@Autowired var movimBankService: MovementBankService,
                            @Autowired var accountBankService: AccountBankService) : FluxoCaixaService {
    override fun geracaoFluxoCaixa(workSpaceId: BigInteger, localDate: LocalDate, qtdDias: Integer): RelatorioDTO {
        var dt = RelatorioDTO(mutableListOf(), mutableListOf())
        val dadosTable = geracaoDataTable(workSpaceId,localDate,qtdDias)
        var headers:MutableList<String> = mutableListOf()
        var lines:MutableList<MutableList<String>> = mutableListOf()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getCurrencyInstance(locale)

        for (i in 0..qtdDias.toLong()) {
            if (i.equals(0)) {
                headers.add("estrutura")
                headers.add("Descrição")
            }
            headers.add(dateFormatter.format(localDate.plusDays(i)))
        }
        dt.headers=headers;
        dadosTable
                .linhas
                .keys
                .groupBy {
                    it.idLinhaFluxo
                }
                .keys
                .forEach {
                    var columns:MutableList<String> = mutableListOf()
                    columns.add(it.estrutura)
                    columns.add(it.descricao)
                    for (i in 0..qtdDias.toLong()) {
                        var acm:BigDecimal = BigDecimal("0")
                        acm = dadosTable
                                .linhas
                                .filter { it1 ->
                                    it1.key.idLinhaFluxo.equals(it) &&
                                            it1.key.day.equals(localDate.plusDays(i))
                                }
                                .values
                                .reduceOrNull { acm, vl -> acm.add(vl) }!!

                        columns.add(numberFormat.format(acm.toDouble()))

                    }
                    lines.add(columns)
                }
        dt.lines=lines

        return dt
    }


    override fun geracaoDataTable (workSpaceId: BigInteger, localDate: LocalDate, qtdDias:Integer):TableDados{
        val dadosTable: TableDados = TableDados();
        // 1- geração dos movimentos no perido solicitado
        movimBankService
                .getAggreeDayAndNatureAndAccount(localDate,
                        localDate.plusDays(qtdDias.toLong()),
                        workSpaceId.toLong())
                .stream()
                .forEach {
                    var ident: ElementoFluxo = ElementoFluxo(IdLinhaFluxo("02." + it.idNatureplan!!.id,
                            it.idNatureplan!!.dsNatureplan!! ), it.idAccountbank!!, it.movementdate!!)
                    dadosTable.plus(ident, it.movementvalue!!)
                }
        // preenchimentos dos elementos vazios com zero
        for ( i in 0..(qtdDias.toLong())) {
            val dta = localDate.plusDays(i)
            var linhas = dadosTable
                    .linhas
                    .keys
                    .groupBy {
                        it.idLinhaFluxo
                    }
                    .keys
            var account= dadosTable
                    .linhas
                    .keys
                    .groupBy { it.account }
                    .keys
            linhas.forEach { itLinha->
                account.forEach { account->
                    dadosTable.plus(ElementoFluxo(itLinha,account,dta), BigDecimal("0"))
                }
            }
        }
        // 2 - geraçáo dos saldos das contas no periodo solicitado
        accountBankService.getBalanceForAccountByWorkSpace(workSpaceId.toLong(),localDate)
                .forEach{
                    var ident = ElementoFluxo(IdLinhaFluxo("01."+it.key.id,
                            it.key.dsAccountbank!!),it.key,localDate)
                    dadosTable.plus(ident,it.value)
                }
        // 03-Totaliza os movimentos nos saldos das contas
        for ( i in 1..(qtdDias.toLong())) {
            val dta = localDate.plusDays(i)
            //Totaliza os movimentos nos saldos das contas
            dadosTable
                    .linhas
                    .filter {
                        it.key.idLinhaFluxo.estrutura.startsWith("01.")
                    }
                    .keys
                    .groupBy { ElementoFluxo(it.idLinhaFluxo,it.account,dta)  }
                    .keys
                    .forEach {
                        var total=dadosTable
                                .linhas
                                .filter {  it1 ->
                                    it1.key.idLinhaFluxo.estrutura.startsWith("02.")
                                            && it1.key.day.equals(it.day)
                                            && it1.key.account!!.id!!.equals(it.account!!.id)
                                }
                                .values
                                .reduceOrNull {  total, it1 ->  total.plus(it1) }
                        dadosTable.plus(it,total?:BigDecimal("0"))
                    }
            // Propagação do Saldo do dia anterior
            dadosTable
                    .linhas
                    .filter {
                        it.key.idLinhaFluxo.estrutura.startsWith("01.")
                                && it.key.day.equals(dta.minusDays(1))
                    }
                    .forEach{
                        dadosTable.plus(ElementoFluxo(it.key.idLinhaFluxo,it.key.account,dta),it.value)
                    }
        }
        return dadosTable;
    }

}