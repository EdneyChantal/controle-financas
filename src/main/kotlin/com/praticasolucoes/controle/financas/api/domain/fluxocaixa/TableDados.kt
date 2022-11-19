package com.praticasolucoes.controle.financas.api.domain.fluxocaixa

import java.math.BigDecimal

data class TableDados(val linhas:MutableMap<ElementoFluxo,BigDecimal>){
    constructor():this(mutableMapOf())
    fun plus(elementoFluxo: ElementoFluxo, valor:BigDecimal){
        if (linhas.contains(elementoFluxo)) {
           var it= linhas.get(elementoFluxo)?.add(valor);
            it?.let { it1 -> linhas.put(elementoFluxo, it1) };
        } else {
            linhas.put(elementoFluxo,valor);
        }
    }
}
