package pt.unl.fct.grantapp.EvaluationPanelTests

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.service.ReviewerService
import org.hamcrest.Matchers.hasSize
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import pt.unl.fct.grantapp.dto.EvaluationPanelDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.EvaluationPanelService
import java.text.SimpleDateFormat
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class EvaluationPanelControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var evaluationPanelService: EvaluationPanelService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6

        val mapper = ObjectMapper().registerModule(KotlinModule())


        val sponsor1 = SponsorEntityDAO(1,"FCT", BCryptPasswordEncoder().encode("FCT"),"SPONSOR","FCT","Costa","@FCT", emptyList())
        val sponsor2 = SponsorEntityDAO(2,"IPS", BCryptPasswordEncoder().encode("IPS"),"SPONSOR","IPS","Setubal","@IPS", emptyList())


        val institution1 = InstitutionDAO()
        val institution2 = InstitutionDAO()

        val reviewer1 = ReviewerDAO(1,"Rev1",BCryptPasswordEncoder().encode("Rev1"),"REVIEWER","Rev1","03/11/1994","rev1@revs.com","address","phone","city","postCode",institution1, emptyList(), emptyList())
        val reviewer2 = ReviewerDAO(1,"Rev2",BCryptPasswordEncoder().encode("Rev2"),"REVIEWER","Rev2","11/03/1994","rev2@revs.com","address","phone","city","postCode",institution2, emptyList(), emptyList())

        val evalPanel1 = EvaluationPanelDAO(1, reviewer1, listOf(reviewer1, reviewer2), emptyList(), emptyList())
        val evalPanel2 = EvaluationPanelDAO(2, reviewer2, listOf(reviewer2, reviewer1), emptyList(), emptyList())

        val evalPanels = ArrayList(listOf(evalPanel1, evalPanel2))

        val evalPanelsDTO = evalPanels.map { EvaluationPanelDTO(it) }

        var date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")
        val grantCall1 = GrantCallDAO(1, "Bolsa FCT","Bolsa maravilhosa para os alunos", "Média >= 14", 999, Date(), date, sponsor1, emptyList(), evalPanel1, emptyList())
        val grantCall2 = GrantCallDAO(2, "Bolsitas Novas","nem sei", "Média > 10", 1, Date(), date, sponsor2, emptyList(), evalPanel2, emptyList())


        val panelURL = "/panel"

    }

    @Test
    fun `Test Get All Panels`(){

        Mockito.`when`(evaluationPanelService.getAll()).thenReturn(evalPanels)

        val result = mvc.perform(get(panelURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(evalPanelsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<EvaluationPanelDTO>>(responseString)
        assertThat(responseDTO, equalTo(evalPanelsDTO))

    }

    @Test
    fun `Test Get One Panel`(){

        Mockito.`when`(evaluationPanelService.getOne(2L)).thenReturn(evalPanel2)

        val result = mvc.perform(get("$panelURL/2"))
                .andExpect(status().isOk)
                .andReturn()
        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<EvaluationPanelDTO>(responseString)
        assertThat(responseDTO, equalTo(evalPanelsDTO[1]))
    }

    @Test
    fun `Test Get One Panel Not Found`(){

        Mockito.`when`(evaluationPanelService.getOne(2L)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$panelURL/2"))
                .andExpect(status().is4xxClientError)

    }

    @Test
    fun `Test Post Panel`(){

    }

    @Test
    fun `Test Delete Panel`(){


        val result = mvc.perform(delete("$panelURL/2"))
                .andExpect(status().isOk)
                .andReturn()

    }
}