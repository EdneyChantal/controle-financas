package com.praticasolucoes.controle.financas.api.domain.matrix


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class LinhaRelatorioTest {

    @Test
    fun addValor() {
        var umaLinha = LinhaRelatorio("um","um relatorio","chave")
        var hoje = LocalDate.now();
        umaLinha.addValor(hoje,1200.0)
        umaLinha.addValor(hoje,1100.0)

        assertEquals(2300.0,umaLinha.totalLinha);
        assertEquals(2300.0, umaLinha.colunas!!.get(hoje)!!.valor)
    }

    @Test
    fun addValor2() {
        var umaLinha = LinhaRelatorio("um","um relatorio","chave")
        var hoje = LocalDate.now();
        umaLinha.addValor(hoje,1200.0)
        umaLinha.addValor(hoje,-1100.0)

        assertEquals(100.0,umaLinha.totalLinha);
        assertEquals(100.0, umaLinha.colunas?.get(hoje)!!.valor)
    }
    @Test
    fun addCelula() {
        var umaLinha = LinhaRelatorio("um","um relatorio","chave")
        var hoje = LocalDate.now()
        var ontem =LocalDate.now().minusDays(1)
        var celulaRelatorio1= CelulaContaRelatorio(hoje,100.0)
        var celularelatorio2= CelulaContaRelatorio(ontem,200.0)


        umaLinha.addCelula(celulaRelatorio1);
        umaLinha.addCelula(celularelatorio2);
        assertEquals(umaLinha.colunas?.size ?: 0,2)
        assertEquals(umaLinha.totalLinha,300.0)

    }


}