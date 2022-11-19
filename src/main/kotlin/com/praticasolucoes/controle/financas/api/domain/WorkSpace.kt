package com.praticasolucoes.controle.financas.api.domain

import javax.persistence.*

@Entity
@Table(name = "tbworkspace")
data class WorkSpace(
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
        var id: Long,
        @Column(name ="description")
        var description:String  )


