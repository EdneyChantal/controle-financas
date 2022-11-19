package com.praticasolucoes.controle.financas.api.services

import com.praticasolucoes.controle.financas.api.domain.fluxocaixa.TableDados
import com.praticasolucoes.controle.financas.api.domain.matrix.LinhaRelatorio
import com.praticasolucoes.controle.financas.api.domain.matrix.RelatorioDTO
import java.math.BigInteger
import java.time.LocalDate

interface FluxoCaixaService {


    fun geracaoFluxoCaixa (workSpaceId:BigInteger , localDate: LocalDate,qtdDias:Integer):RelatorioDTO
    fun geracaoDataTable (workSpaceId: BigInteger, localDate: LocalDate , qtdDias:Integer):TableDados
}