package com.praticasolucoes.controle.financas.api.services.impl


import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import com.praticasolucoes.controle.financas.api.domain.Tbmovementbank
import com.praticasolucoes.controle.financas.api.domain.Tbnatureplan
import com.praticasolucoes.controle.financas.api.domain.matrix.LinhaRelatorio
import com.praticasolucoes.controle.financas.api.repository.TbmovementbankRepository
import com.praticasolucoes.controle.financas.api.services.MovementBankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class MovimentBankServiceImpl(@Autowired var movRepo:TbmovementbankRepository):MovementBankService{
    override fun getBalanceForAccountDay(acc: Tbaccountbank, day: LocalDate): BigDecimal {
        return movRepo.getBalanceDay(acc,day ).add(acc.initialValue?:BigDecimal("0"))
    }
   override fun getAggreeDayAndNatureAndAccount(dayIni: LocalDate, dayFim: LocalDate, workSpaceId: Long): List<Tbmovementbank> {
        var list:List<Array<Object>> = movRepo.getAgreeFromDayAndNatureAndAccount(dayIni,dayFim,workSpaceId);
        return list.map { Tbmovementbank(null, it.get(2) as Tbaccountbank,
                it.get(1) as Tbnatureplan,
                it.get(0) as LocalDate,
                it.get(3) as BigDecimal,null,null,null,null,null) }
    }
    override fun fromListMovementToListLinhaRelatorio(listM: List<Tbmovementbank>): List<LinhaRelatorio> {
       val setLinha:MutableMap<String,LinhaRelatorio> = mutableMapOf();
       listM.forEach{ addLinhaRelatorio(it,setLinha)  }
       return setLinha.values.toList()
    }
    private fun addLinhaRelatorio(a:Tbmovementbank,b:MutableMap<String,LinhaRelatorio>) {
        val chave:String = "3."+ a!!.idNatureplan!!.id
        if (b.contains(chave)) {
            a.movementdate?.let { a.idAccountbank?.let {
                it1 -> b.get(chave)!!.addValor(it,a.movementvalue!!.toDouble(), it1)
            } }
        } else {
           var lin = LinhaRelatorio(chave,
                    a!!.idNatureplan!!.dsNatureplan?:"",chave)
            a.movementdate?.let { a.idAccountbank?.let {
                it1 -> lin.addValor(it,a.movementvalue!!.toDouble(), it1)
            } }
           b.put(chave,lin)
       }
    }
}