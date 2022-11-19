package com.praticasolucoes.controle.financas.api.services.impl

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import com.praticasolucoes.controle.financas.api.repository.TbaccountbankRepository
import com.praticasolucoes.controle.financas.api.services.AccountBankService
import com.praticasolucoes.controle.financas.api.services.MovementBankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class AccountBankServiceImpl(@Autowired var accountRep: TbaccountbankRepository,
                             @Autowired var movementBankService: MovementBankService) : AccountBankService {
    override fun getAllBalanceForWorkSpace(workspaceId: Long, localDate: LocalDate): BigDecimal {
        var listAccount: List<Tbaccountbank> = this.accountRep.findByWorkSpaceId(workspaceId)
        val balance: BigDecimal
        return listAccount.map { it -> movementBankService.getBalanceForAccountDay(it, localDate) }
                .reduce { balance, it ->
                    balance.add(it)
                }
    }

    override fun getBalanceForAccountByWorkSpace(workspaceId: Long, localDate: LocalDate): Map<Tbaccountbank, BigDecimal> {
        var listAccount: List<Tbaccountbank> = this.accountRep.findByWorkSpaceId(workspaceId)
        var balacesList: MutableMap<Tbaccountbank, BigDecimal> = mutableMapOf()
        val balance: BigDecimal
        listAccount
                .stream()
                .forEach { it ->
                    balacesList.put(it, movementBankService.getBalanceForAccountDay(it, localDate))
                }
        return balacesList
    }
}