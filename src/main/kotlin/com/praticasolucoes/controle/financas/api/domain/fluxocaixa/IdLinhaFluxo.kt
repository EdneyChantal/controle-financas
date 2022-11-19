package com.praticasolucoes.controle.financas.api.domain.fluxocaixa

data class IdLinhaFluxo(var estrutura:String,var descricao:String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdLinhaFluxo

        if (estrutura != other.estrutura) return false
        if (descricao != other.descricao) return false

        return true
    }

    override fun hashCode(): Int {
        var result = estrutura.hashCode()
        result = 31 * result + descricao.hashCode()
        return result
    }
}