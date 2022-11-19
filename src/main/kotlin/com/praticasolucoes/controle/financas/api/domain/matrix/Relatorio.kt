package com.praticasolucoes.controle.financas.api.domain.matrix

import java.text.NumberFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

class Relatorio(val nomeRelatorio: String,
                var linhas: MutableSet<LinhaRelatorio>,
                val timeReferences: LocalDate ,
                val saldoInicial:Double,
                val linhaSaldoInicial:LinhaRelatorio?,
                val linhaSaldoFinal:LinhaRelatorio?) {
    var dateStart: LocalDate = LocalDate.now()
    var dateEnd: LocalDate = LocalDate.now()
    lateinit var totalColunasMap: MutableMap<LocalDate, Double>
    lateinit var saldoInicialMap: MutableMap<LocalDate, Double>
    lateinit var saldoFinalMap: MutableMap<LocalDate, Double>

    constructor(nomeRelatorio: String) : this(nomeRelatorio, mutableSetOf(), LocalDate.now(),0.0,null,null)
    constructor(nomeRelatorio: String,
                timeReferences: LocalDate,
                saldoInicial: Double,
                linhaSaldoInicial: LinhaRelatorio?,
                linhaSaldoFinal: LinhaRelatorio?) :
            this(nomeRelatorio, mutableSetOf(),
                    timeReferences,saldoInicial,
                    linhaSaldoInicial,linhaSaldoFinal)

    init {
        fillStarEndDate()
    }

    private fun adicionaObjectMap(dia: LocalDate
                             ,valor: Double
                             ,map:MutableMap<LocalDate, Double>) {
        if ( dateStart.compareTo(dia ) <=0
             && dateEnd.compareTo(dia) >= 0 ) {
            if (map.containsKey(dia)) {
                map.put(dia, map.getValue(dia).plus(valor))
            } else {
                map.put(dia, valor);
            }
        }
    }
    private fun fillAlllines() {
        totalColunasMap= mutableMapOf()
        saldoInicialMap = mutableMapOf()
        saldoFinalMap = mutableMapOf()
        linhas
                .stream()
                .forEach { linha ->
                    run {
                        for (i in dateStart.dayOfMonth..dateEnd.dayOfMonth) {
                            var da: LocalDate = dateStart.withDayOfMonth(i)
                            var cel: CelulaContaRelatorio = CelulaContaRelatorio(da, 0.000)
                            if (!linha.colunas?.contains(cel.dia)!!) {
                                linha.addCelula(cel)
                                adicionaObjectMap(da,0.0,totalColunasMap)
                            } else {
                                adicionaObjectMap(da,linha!!.colunas!!.get(da)!!.valor,totalColunasMap)

                            }

                        }
                    }
                }
        adicionaObjectMap(dateStart,saldoInicial,saldoInicialMap)
        for (i in dateStart.dayOfMonth..dateEnd.dayOfMonth ) {
            var da: LocalDate = dateStart.withDayOfMonth(i)
            var sld = saldoInicialMap.get(da)
            sld = totalColunasMap.get(da)?.let { sld?.plus(it) ?: 0.0 }
            sld?.let { adicionaObjectMap(da, it,saldoFinalMap) }
            try {
                sld?.let { adicionaObjectMap(da.withDayOfMonth(i + 1), it, saldoInicialMap) }
            } catch (ex:DateTimeException) {

            }
        }
    }
    private fun fillStarEndDate() {
        dateStart = timeReferences.withDayOfMonth(1)
        dateEnd = timeReferences.with(TemporalAdjusters.lastDayOfMonth())
    }

    fun adicionaCelulaLinha(linhaRelatorio: LinhaRelatorio, celulaRelatorio: CelulaContaRelatorio) {
        if (this.linhas.contains(linhaRelatorio)) {
            this.linhas.stream().forEach {
                if (it.equals(linhaRelatorio)) {
                    it.addCelula(celulaRelatorio)
                }
            }
        } else {
            linhaRelatorio.addCelula(celulaRelatorio);
            this.linhas.add(linhaRelatorio)
        }
    }
    private fun montaHeaders():MutableList<String>{
        var formatter:DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM")
        var headers:MutableList<String> = mutableListOf()
        headers.add("Id")
        headers.add("Descricao")
        for (i in dateStart.dayOfMonth..dateEnd.dayOfMonth ) {
            var da:LocalDate=dateStart.withDayOfMonth(i);
            headers.add(formatter.format(da))
        }
        return headers;
    }
    private fun montaLinhaResume(linha:LinhaRelatorio,resume:Map<LocalDate,Double>):MutableList<String>{
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        var colunas:MutableList<String> = mutableListOf()
        colunas.add(linha?.id ?: "")
        colunas.add(linha?.descricao ?: "")
        for (saldo in resume ){
            colunas.add(numberFormat.format(saldo.value))
        }
        return colunas
    }
    private fun montaLines():MutableList<MutableList<String>> {
        var lines:MutableList<MutableList<String>> = mutableListOf()
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        linhaSaldoInicial?.let { montaLinhaResume(it,saldoInicialMap) }?.let { lines.add(it) }
        linhaSaldoFinal?.let { montaLinhaResume(it,saldoFinalMap) }?.let { lines.add(it) }
        linhas
                .stream()
                .forEach {
                    var colunas:MutableList<String> = mutableListOf()
                    colunas.add(it.id)
                    colunas.add(it.descricao)
                    it.colunas!!.keys.stream()?.forEach {
                        colunas.add(numberFormat.format(it)   )
                    }
                    lines.add(colunas)
                }
        return lines;
    }
    fun toRelatorioDTO():RelatorioDTO {
        this.fillAlllines()
        return RelatorioDTO(this.montaHeaders(),this.montaLines())
    }
}