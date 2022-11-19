package com.praticasolucoes.controle.financas.api.domain

import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "tbmovementbank")
data class Tbmovementbank (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmovementbank", nullable = false)
    var id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_accountbank")
     var idAccountbank: Tbaccountbank? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_natureplan")
     var idNatureplan: Tbnatureplan? = null,

    @Column(name = "movementdate", nullable = false)
     var movementdate: LocalDate? = null,

    @Column(name = "movementvalue", precision = 30, scale = 2)
     var movementvalue: BigDecimal? = null,

    @Column(name = "history", length = 200)
     var history: String? = null,

    @Column(name = "number_doc", length = 100)
     var numberDoc: String? = null,

    @Column(name = "accid", length = 100)
     var accid: String? = null,
    @Transient var idaccountbank:Int? , @Transient var idnatureplan:Int?
)