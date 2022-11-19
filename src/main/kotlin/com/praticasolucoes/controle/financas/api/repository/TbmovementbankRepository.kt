package com.praticasolucoes.controle.financas.api.repository;

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import com.praticasolucoes.controle.financas.api.domain.Tbmovementbank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
interface TbmovementbankRepository : JpaRepository<Tbmovementbank, Int> {

    @Query("select coalesce(sum(d.movementvalue),0) from Tbmovementbank d where d.idAccountbank =?1 and d.movementdate<= ?2")
    fun getBalanceDay(account:Tbaccountbank,day:LocalDate):BigDecimal

    @Query("select d.movementdate,d.idNatureplan,sum(d.movementvalue) " +
            "from Tbmovementbank d where d.movementdate >= ?1 and " +
            "d.movementdate <= ?2 and d.idAccountbank.idWorkspace.id = ?3 " +
            "group by d.movementdate,d.idNatureplan")
    fun getAgreeFromDayAndNature(dayIni:LocalDate,dayFim:LocalDate,workspaceId:Long):List<Array<Object>>
    @Query( "select d.movementdate,d.idNatureplan,d.idAccountbank,sum(d.movementvalue) " +
            "from Tbmovementbank d " +
            "where d.movementdate >= ?1 and d.movementdate <= ?2 and d.idAccountbank.idWorkspace.id = ?3 " +
            "group by d.movementdate,d.idNatureplan,d.idAccountbank")
    fun getAgreeFromDayAndNatureAndAccount(dayIni:LocalDate,dayFim:LocalDate,workspaceId:Long):List<Array<Object>>
}