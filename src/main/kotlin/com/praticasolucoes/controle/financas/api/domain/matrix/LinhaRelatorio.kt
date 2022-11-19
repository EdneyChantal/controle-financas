package com.praticasolucoes.controle.financas.api.domain.matrix

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import java.time.LocalDate

data class LinhaRelatorio (val id:String,
                           val descricao:String,
                           val chave:String,
                           var colunas:MutableMap<LocalDate,CelulaContaRelatorio>?)  {
    constructor(id: String, descricao: String, chave: String) : this(id,descricao,chave, mutableMapOf<LocalDate,CelulaContaRelatorio>())
    var totalLinha:Double =0.0
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false ;
        }
        if (other is LinhaRelatorio) {
            if (other.id == this.id && other.chave == this.chave) {
                return true;
            }
        }
        return false;
    }
    fun addValor(dia:LocalDate,valor:Double,account:Tbaccountbank) {
        var cel = CelulaContaRelatorio(dia,0.00);
        cel.addValorConta(account,valor)
        addCelula(cel,account)
    }
    fun addValor(dia:LocalDate,valor:Double) {
        addCelula(CelulaContaRelatorio(dia,valor))
    }
    fun addCelula(celulaRelatorio: CelulaContaRelatorio) {
        if (colunas!!.containsKey(celulaRelatorio.dia )) {
            this.colunas!!.get(celulaRelatorio.dia)!!.add(celulaRelatorio)
        } else {
            colunas!!.put(celulaRelatorio.dia,celulaRelatorio);
        }
        this.totalLinha = this.totalLinha.plus(celulaRelatorio.valor)
    }
    fun addCelula(celulaRelatorio: CelulaContaRelatorio , tbaccountbank: Tbaccountbank) {
        if (colunas!!.containsKey(celulaRelatorio.dia )) {
            var va = this.colunas!!.get(celulaRelatorio.dia);
            va!!.addValorConta(tbaccountbank,celulaRelatorio.valor);
            if (va != null) {
                this.colunas!!.put(celulaRelatorio.dia,va)
            };
        } else {
            colunas!!.put(celulaRelatorio.dia,celulaRelatorio);
        }
        this.totalLinha = this.totalLinha.plus(celulaRelatorio.valor)
    }
}