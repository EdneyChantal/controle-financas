package com.praticasolucoes.controle.financas.api.domain.matrix

data class RelatorioDTO (var headers:List<String>,var lines:List<List<String>>)  {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RelatorioDTO

        if (headers != other.headers) return false
        if (lines != other.lines) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headers.hashCode()
        result = 31 * result + lines.hashCode()
        return result
    }

}