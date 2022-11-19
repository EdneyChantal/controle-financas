package com.praticasolucoes.controle.financas.api.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "tbaccountbank")
data class Tbaccountbank (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_accountbank", nullable = false)
    var id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_workspace")
    var idWorkspace: WorkSpace? = null,

    @Column(name = "ds_accountbank", length = 50)
    var dsAccountbank: String? = null,

    @Column(name = "initial_value", precision = 30, scale = 2)
    var initialValue: BigDecimal? = null,

    @Column(name = "cod_conta", length = 30)
    var codConta: String? = null,

    @Column(name = "digito", length = 10)
    var digito: String? = null,

    @Column(name = "tipo", nullable = false, length = 10)
    var tipo: String? = null ,
    @Transient var idworkspace:Int?
      )
