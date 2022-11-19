package com.praticasolucoes.controle.financas.api.domain.fluxocaixa

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import java.time.LocalDate

data class ElementoFluxo(var idLinhaFluxo: IdLinhaFluxo,
                         var account: Tbaccountbank?,
                         var day: LocalDate) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ElementoFluxo

        if (idLinhaFluxo != other.idLinhaFluxo) return false
        if (account != other.account) return false
        if (day != other.day) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idLinhaFluxo.hashCode()
        result = 31 * result + (account?.hashCode() ?: 0)
        result = 31 * result + day.hashCode()
        return result
    }
}
