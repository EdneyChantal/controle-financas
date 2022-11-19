package com.praticasolucoes.controle.financas.api.repository;

import com.praticasolucoes.controle.financas.api.domain.WorkSpace
import org.springframework.data.jpa.repository.JpaRepository

interface WorkSpaceRepository : JpaRepository<WorkSpace, Long> {
}