package com.praticasolucoes.controle.financas.api.domain.matrix

import com.praticasolucoes.controle.financas.api.domain.Tbaccountbank
import com.praticasolucoes.controle.financas.api.domain.Tbmovementbank
import com.praticasolucoes.controle.financas.api.domain.Tbnatureplan
import com.praticasolucoes.controle.financas.api.domain.WorkSpace
import com.praticasolucoes.controle.financas.api.repository.TbaccountbankRepository
import com.praticasolucoes.controle.financas.api.repository.TbmovementbankRepository
import com.praticasolucoes.controle.financas.api.repository.TbnatureplanRepository
import com.praticasolucoes.controle.financas.api.repository.WorkSpaceRepository
import com.praticasolucoes.controle.financas.api.services.AccountBankService
import com.praticasolucoes.controle.financas.api.services.FluxoCaixaService
import com.praticasolucoes.controle.financas.api.services.MovementBankService
import io.cucumber.datatable.DataTable
import io.cucumber.java.DataTableType
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@CucumberContextConfiguration
@SpringBootTest
class RelatorioSteps(@Autowired val workrepo: WorkSpaceRepository?,
                     @Autowired val natuRepo: TbnatureplanRepository,
                     @Autowired val accountRepo: TbaccountbankRepository,
                     @Autowired val movimentRepo:TbmovementbankRepository,
                     @Autowired val movimService:MovementBankService,
                     @Autowired val accountSer:AccountBankService,
                     @Autowired val fluxoCaixaService: FluxoCaixaService
                     ) {
    @DataTableType(replaceWithEmptyString = ["[blank]"])
    fun convertWorkSpace(entry: Map<String, String>): WorkSpace? {
        return entry.get("Description")?.let { WorkSpace(entry.get("id")!!.toLong(), it) }
    }

    @DataTableType(replaceWithEmptyString = ["[blank]"])
    fun convertNature(entry: Map<String, String>): Tbnatureplan? {
        return Tbnatureplan(entry.get("id")!!.toInt(), null, entry.get("dsNatureplan"), null,
                entry.get("indType"), null, entry.get("idWorkspace")!!.toInt())
    }
    @DataTableType(replaceWithEmptyString = ["[blank]"])
    fun convertAccount(entry: Map<String, String>): Tbaccountbank? {
        return Tbaccountbank(entry.get("id")!!.toInt(),
                null,
                entry.get("dsAccountbank"),
                entry.get("initialValue")!!.replace(",",".").toBigDecimal(),
                null,
                null,
                entry.get("tipo"),
                entry.get("idWorkspace")!!.toInt())

    }
    @DataTableType(replaceWithEmptyString = ["[blank]"])
    fun convertMovimentoBanck(entry: Map<String, String>): Tbmovementbank? {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return Tbmovementbank(entry.get("id")!!.toInt(),
                null,
                null,
                LocalDate.parse(entry.get("movementdate"), dateFormatter),
                entry.get("movementvalue")!!.replace(",",".")!!.toBigDecimal(),
                null, null, null,
                entry.get("idAccountbank")!!.toInt(),
                entry.get("idNatureplan")!!.toInt())
    }
    @Given("workspace")
    fun workspace(dataTable: List<WorkSpace>) {
        workrepo?.saveAll(dataTable)
    }
    @Given("plano de natureza")
    fun plano_de_natureza(dataTable: List<Tbnatureplan>) {
        dataTable.stream().forEach { it.idWorkspace = workrepo!!.getReferenceById(it.idworkspace!!.toLong()) }
        natuRepo.saveAll(dataTable)
    }

    @Given("conta bancaria")
    fun conta_bancaria(dataTable: List<Tbaccountbank>) {
        dataTable.stream().forEach { it.idWorkspace = workrepo!!.getReferenceById(it.idworkspace!!.toLong()) }
        accountRepo.saveAll(dataTable)
    }

    @Given("Movimento Bancario")
    fun movimento_bancario(dataTable: List<Tbmovementbank>?) {
        dataTable!!.stream().forEach { it.idNatureplan = natuRepo.getReferenceById(it.idnatureplan!!)
                                        it.idAccountbank = accountRepo.getReferenceById(it.idaccountbank!!)
        }
        var saveAll = movimentRepo.saveAll(dataTable)
        val ret:BigDecimal = accountSer.getAllBalanceForWorkSpace(1
        , LocalDate.of(2022,1,20))

        val x:List<Tbmovementbank> = movimService.getAggreeDayAndNatureAndAccount(
                         LocalDate.of(2022,1,10),
                        LocalDate.of(2022,1,20),1);

        val fromListMovementToListLinhaRelatorio = this.movimService.fromListMovementToListLinhaRelatorio(x);


    }
    @When("gerarRelatorioFluxoCaixa em {string}")
    fun gerar_relatorio_fluxo_caixa_em(data:String) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var dto:RelatorioDTO=fluxoCaixaService.geracaoFluxoCaixa(BigInteger("1"),
                LocalDate.parse(data, dateFormatter),Integer("5"))

    }
    @Then("deve sair um relatório")
    fun deve_sair_um_relatório(dataTable: DataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        var esperado:MutableList<MutableList<String>> = dataTable.asLists();
        var relatorio:RelatorioDTO= RelatorioDTO(esperado.get(0),
                                    esperado.
                                    subList(1,esperado.size-1).map { it.map { it ?: "x" }  }
                                    );
    }

}