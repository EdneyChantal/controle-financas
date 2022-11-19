package com.praticasolucoes.controle.financas.api.domain.matrix

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

internal class RelatorioTest {

    @Test
    fun adicionaLinha() {
        var umaLinha = LinhaRelatorio("um","um relatorio","chave")
        var hoje = LocalDate.now();
        umaLinha.addValor(hoje,1200.0)
        var outraCeula2 = CelulaContaRelatorio(hoje, 120.0)
        var relatorio = Relatorio("tesgte")
        relatorio.adicionaCelulaLinha(umaLinha,outraCeula2)
        assertEquals(1320.0,relatorio.linhas.first().totalLinha)
        assertEquals(1320.0, relatorio.linhas.first().colunas?.get(hoje)?.valor ?: 0)
    }
    @Test
    fun fillWithZeros() {
        var umaLinha = LinhaRelatorio("um","um relatorio","chave")
        var hoje = LocalDate.now();

        umaLinha.addValor(hoje,1200.0)
        var outraCeula2 = CelulaContaRelatorio(hoje, 120.0)
        var relatorio = Relatorio("tesgte",hoje,1000.0,
                LinhaRelatorio("1","Saldo Inicial","1"),
                LinhaRelatorio("2","SaldoFinal","2"))

        var outraLinha  = LinhaRelatorio("dois","outro","chave2")


        relatorio.adicionaCelulaLinha(umaLinha,outraCeula2)
        relatorio.adicionaCelulaLinha(outraLinha,outraCeula2);


        var rel:RelatorioDTO=relatorio.toRelatorioDTO()
        var v2:ObjectMapper= ObjectMapper();
        assertEquals(2,relatorio.linhas.size)
        assertEquals(hoje.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth, relatorio.linhas.first().colunas?.size ?: 0)
        assertEquals(1440.0,relatorio.totalColunasMap.get(hoje))
        System.out.println(v2.writeValueAsString(rel))

    }

}