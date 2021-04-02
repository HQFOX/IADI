package pt.unl.fct.grantapp.ApplicationTests

import pt.unl.fct.grantapp.SecurityTests.SecurityTests
import pt.unl.fct.grantapp.dto.ApplicationDTO
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.ApplicationService

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.grantapp.GrantCallTests.GrantCallControllerTest
import pt.unl.fct.grantapp.dto.GrantCallDTO
import pt.unl.fct.grantapp.dto.StudentApplicationDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.*
import pt.unl.fct.grantapp.service.GrantCallService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var applicationRepository: ApplicationRepository

    @Autowired
    lateinit var evals: EvaluationPanelRepository

    @Autowired
    lateinit var students: StudentRepository

    @Autowired
    lateinit var grants: GrantCallRepository

    @Autowired
    lateinit var sponsors: SponsorEntityRepository


    @MockBean
    lateinit var applicationService: ApplicationService

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())

        var date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")

        val institution = InstitutionDAO(1L, "FCT", BCryptPasswordEncoder().encode("FCT"), "INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())
        val sponsor = SponsorEntityDAO(1, "FCT", BCryptPasswordEncoder().encode("FCT"), "SPONSOR", "FCT", "Costa", "@FCT", emptyList())
        val reviewer = ReviewerDAO(1, "Rev1", BCryptPasswordEncoder().encode("Rev1"), "REVIEWER", "Rev1", "03/11/1994", "rev1@revs.com", "address", "phone", "city", "postCode", institution, emptyList(), emptyList())
        val evalPanel = EvaluationPanelDAO(1, reviewer, listOf(reviewer), emptyList(), emptyList())
        val grantCall = GrantCallDAO(1, "Bolsa FCT", "Bolsa maravilhosa para os alunos", "Media >= 14", 999, Date(), date, sponsor, emptyList(), evalPanel, emptyList())
        val grantCall2 = GrantCallDAO(2, "Bolsa IPS", "Bolsa maravilhosa para os alunos", "Media >= 99", 1, Date(), date, sponsor, emptyList(), evalPanel, emptyList())

        val student1 = StudentDAO(1, "stud1", BCryptPasswordEncoder().encode("stud1"), "STUDENT",
                "Student1", "birthday", "email", "address", "telephone", "city", "postcode",
                institution, emptyList())

        val applicationDAO = ApplicationDAO(1, "Very good intro", "Field work", "Sleep", "none", "Waiting Response", student1, grantCall, emptyList(), emptyList())

        val apps = mutableListOf(applicationDAO)

        val appsDTO = ApplicationDTO(applicationDAO)
        val applicationURL = "/application"
    }

    @Test
    fun `Test get all aplications (No Role)`() {
        Mockito.`when`(applicationService.getAll()).thenReturn(apps)

        mvc.perform(MockMvcRequestBuilders.get(applicationURL))
                .andExpect(status().isOk())
    }

    @Test
    fun `Test get one application (Not Found)`() {
        Mockito.`when`(applicationService.getOne(77)).thenThrow(NotFoundException("not found"))

        mvc.perform(MockMvcRequestBuilders.get("${applicationURL}/77"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun `Test get one application (Found)`() {
        /**
        Mockito.`when`(applicationService.getOne(1)).thenReturn(applicationDAO)

        val result = mvc.perform(MockMvcRequestBuilders.get("${applicationURL}/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ApplicationDTO>(responseString)
        assertEquals(responseDTO, appsDTO[0])
        **/
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Transactional
    @Test
    fun `Test create application (NO ROLE)`() {

        val sponsor = sponsors.save(SponsorEntityDAO())
        val eval = evals.save(EvaluationPanelDAO())
        val student = students.save(StudentDAO())
        val grant = GrantCallDTO(0, "title","desc","reqs",999,
                Date(),Date(), sponsor.id, eval.id, 0)
        val grantDAO = grants.save(GrantCallDAO(grant, sponsor, emptyList(), eval))

        val appDAO = ApplicationDAO(1,"introducao","work","work","publicacoes","status",student,grantDAO, emptyList(), emptyList())

        val studentApplicationDTO = StudentApplicationDTO(student.id,grant.grantCallId, ApplicationDTO(appDAO) , emptyList())

        val appJson = mapper.writeValueAsString(studentApplicationDTO)

        Mockito.`when`(applicationService.addOne(nonNullAny(ApplicationDAO::class.java)))
                .then { assertEquals(it.getArgument(0), appDAO); it.getArgument(0) }

        mvc.perform(post("$applicationURL")
                .contentType(MediaType.APPLICATION_JSON)
                .content(appJson))
                //.andExpect(status().isUnauthorized)
                .andExpect(status().isForbidden)

    }

    @Test
    fun `Test update application status`() {


    }

}