package com.praticasolucoes.controle.financas.api.repository;

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigInteger

interface TbaccountbankRepository : JpaRepository<Tbaccountbank, Int> {

    @Query("Select a from Tbaccountbank  a where a.idWorkspace.id = ?1")
    fun findByWorkSpaceId(workSpaceId:Long):List<Tbaccountbank>
}