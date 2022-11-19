package com.praticasolucoes.controle.financas.api.services

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import java.math.BigDecimal
import java.time.LocalDate

interface AccountBankService {
    fun getAllBalanceForWorkSpace(workspaceId:Long, localDate: LocalDate):BigDecimal
    fun getBalanceForAccountByWorkSpace(workspaceId: Long, localDate: LocalDate): Map<Tbaccountbank,BigDecimal>
}