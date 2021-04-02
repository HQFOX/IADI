package pt.unl.fct.grantapp.EvaluationPanelTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.grantapp.dto.EvaluationPanelDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.EvaluationPanelRepository
import pt.unl.fct.grantapp.model.GrantCallRepository
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.EvaluationPanelService
import pt.unl.fct.grantapp.service.GrantCallService
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class EvaluationPanelServiceTest {

    @Autowired
    lateinit var panelService: EvaluationPanelService

    @MockBean
    lateinit var panelRepo: EvaluationPanelRepository

    companion object Constants {

        val mapper = ObjectMapper().registerModule(KotlinModule())


        val sponsor1 = SponsorEntityDAO(1,"FCT", BCryptPasswordEncoder().encode("FCT"),"SPONSOR","FCT","Costa","@FCT", emptyList())
        val sponsor2 = SponsorEntityDAO(2,"IPS", BCryptPasswordEncoder().encode("IPS"),"SPONSOR","IPS","Setubal","@IPS", emptyList())


        val institution1 = InstitutionDAO(1L,"FCT",BCryptPasswordEncoder().encode("FCT"),"INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())
        val institution2 = InstitutionDAO(1L,"FCT",BCryptPasswordEncoder().encode("FCT"),"INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())

        val reviewer1 = ReviewerDAO(1,"Rev1", BCryptPasswordEncoder().encode("Rev1"),"REVIEWER","Rev1","03/11/1994","rev1@revs.com","address","phone","city","postCode",institution1, emptyList(), emptyList())
        val reviewer2 = ReviewerDAO(1,"Rev2", BCryptPasswordEncoder().encode("Rev2"),"REVIEWER","Rev2","11/03/1994","rev2@revs.com","address","phone","city","postCode",institution2, emptyList(), emptyList())

        val evalPanel1 = EvaluationPanelDAO(1, reviewer1, listOf(reviewer1, reviewer2), emptyList(), emptyList())
        val evalPanel2 = EvaluationPanelDAO(2L, reviewer2, listOf(reviewer2, reviewer1), emptyList(), emptyList())

        val evalPanels = mutableListOf(evalPanel1, evalPanel2)

        val evalPanelsDTO = evalPanels.map { EvaluationPanelDTO(it) }

        var date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")
        val grantCall1 = GrantCallDAO(1, "Bolsa FCT","Bolsa maravilhosa para os alunos", "Média >= 14", 999, Date(), date, sponsor1, emptyList(), evalPanel1, emptyList())
        val grantCall2 = GrantCallDAO(2, "Bolsitas Novas","nem sei", "Média > 10", 1, Date(), date, sponsor2, emptyList(), evalPanel2, emptyList())


    }

    @Test
    fun `get all evaluation panels`() {
        Mockito.`when`(panelRepo.findAll()).thenReturn(evalPanels);

        assertThat(panelService.getAll(), equalTo(evalPanels))
    }

    @org.junit.Test
    fun `get one evaluation panel`(){
        Mockito.`when`(panelRepo.findById(2L)).thenReturn(Optional.of(evalPanel2));

        assertThat(panelService.getOne(2L), equalTo(evalPanel2))
    }

    @Test(expected = NotFoundException::class)
    fun `get one evaluation panel non existent`() {
        Mockito.`when`(panelRepo.findById(anyLong())).thenReturn(Optional.empty())
        panelService.getOne(999)
    }


    @Test
    fun `test on adding a new panel`(){
        Mockito.`when`(panelRepo.save(Mockito.any(EvaluationPanelDAO::class.java)))
                .then {
                    val panel: EvaluationPanelDAO = it.getArgument(0)
                    assertThat(panel.id, equalTo(evalPanel1.id))
                    assertThat(panel.chair, equalTo(evalPanel1.chair))
                    assertThat(panel.members, equalTo(evalPanel1.members))
                    assertThat(panel.grantCalls, equalTo(evalPanel1.grantCalls))
                    assertThat(panel.reviews, equalTo(evalPanel1.reviews))
                    panel
                }
        //panelService.addOne(EvaluationPanelDAO(0, evalPanel1.chair, evalPanel1.members, evalPanel1.grantCalls, evalPanel1.reviews))
        panelService.addOne(evalPanel1)
    }

}