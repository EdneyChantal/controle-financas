package com.praticasolucoes.controle.financas.api.domain.matrix

import java.time.LocalDate

open class CelulaRelatorio(val dia: LocalDate = LocalDate.now(), var valor: Double) : Comparable<CelulaRelatorio> {


    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is CelulaRelatorio) {
            if (other.dia.equals(this.dia)) {
                return true
            }
        }
        return false
    }
    override fun hashCode(): Int {
        return this.dia.hashCode();
    }
    open fun add(item: CelulaRelatorio) {
        if (item.dia.equals(this.dia)) {
            this.valor = this.valor.plus(item.valor)
        }
    }
    open fun soma(parametro: Double) {
        valor = valor.plus(parametro)
    }
    override fun compareTo(other: CelulaRelatorio): Int {
        return this.dia.compareTo(other.dia)
    }
}