package com.praticasolucoes.controle.financas.api.domain.matrix

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import java.time.LocalDate

class CelulaContaRelatorio: CelulaRelatorio {
    constructor(dia:LocalDate,valor:Double): super(dia,valor)
    constructor(dia:LocalDate):super(dia,0.0)
    var valorPorConta:MutableMap<Tbaccountbank,Double> = mutableMapOf()
    fun addValorConta(acccount:Tbaccountbank,valor: Double){
        if (valorPorConta.contains(acccount)) {
            valorPorConta.put(acccount,valorPorConta.get(acccount)!!.plus(valor))
        } else {
            valorPorConta.put(acccount,valor)
        }
        this.valor = this.valor.plus(valor)
    }

}