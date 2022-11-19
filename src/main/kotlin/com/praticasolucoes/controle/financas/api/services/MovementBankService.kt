package com.praticasolucoes.controle.financas.api.services

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import com.praticasolucoes.controle.financas.api.domain.Tbmovementbank
import com.praticasolucoes.controle.financas.api.domain.matrix.LinhaRelatorio
import java.math.BigDecimal
import java.time.LocalDate

interface MovementBankService {

    fun getBalanceForAccountDay(acc:Tbaccountbank,day:LocalDate):BigDecimal
    fun getAggreeDayAndNatureAndAccount(dayIni:LocalDate,dayFim:LocalDate,workSpaceId:Long):List<Tbmovementbank>
    fun fromListMovementToListLinhaRelatorio(listM:List<Tbmovementbank>):List<LinhaRelatorio>
}