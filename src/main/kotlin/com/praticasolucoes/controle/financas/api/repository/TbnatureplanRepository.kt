package com.praticasolucoes.controle.financas.api.repository;

import com.praticasolucoes.controle.financas.api.domain.Tbnatureplan
import org.springframework.data.jpa.repository.JpaRepository

interface TbnatureplanRepository : JpaRepository<Tbnatureplan, Int> {
}