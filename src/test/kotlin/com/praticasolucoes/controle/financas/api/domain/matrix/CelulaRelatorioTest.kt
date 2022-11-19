package com.praticasolucoes.controle.financas.api.domain.matrix

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class CelulaRelatorioTest {


    @Test
    fun addParamNullDoNothing() {
        var hoje = LocalDate.now();
        var umaCelula = CelulaRelatorio(hoje, 0.0)
        var outraCeula = CelulaRelatorio(hoje, 100.0)
        var outraCeula2 = CelulaRelatorio(hoje, 120.0)

        umaCelula.add(outraCeula)
        umaCelula.add(outraCeula2)
        assertEquals(220.0, umaCelula.valor)

    }
    @Test
    fun addDiferencesDays() {
        var hoje = LocalDate.now();
        var amanha = LocalDate.now().minusDays(1);
        var umaCelula = CelulaRelatorio(hoje, 0.0)
        var outraCeula = CelulaRelatorio(amanha, 100.0)
        var outraCeula2 = CelulaRelatorio(hoje, 120.0)

        umaCelula.add(outraCeula)
        umaCelula.add(outraCeula2)
        assertEquals(120.0, umaCelula.valor)

    }

}