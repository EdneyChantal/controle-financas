package com.praticasolucoes.controle.financas.api.domain

import javax.persistence.*

@Entity
@Table(name = "tbnatureplan")
data class Tbnatureplan (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_natureplan", nullable = false)
    var id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_workspace", nullable = false)
    var idWorkspace: WorkSpace? = null,

    @Column(name = "ds_natureplan", length = 50)
    var dsNatureplan: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_natureplan_father")
    var idNatureplanFather: Tbnatureplan? = null,

    @Column(name = "ind_type", nullable = false, length = 1)
    var indType: String? = null,

    @Column(name = "ind_control_orcamento", length = 2)
    var indControlOrcamento: String? = null,
    @Transient var idworkspace:Int?
)