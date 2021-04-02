package pt.unl.fct.grantapp.SecurityTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.grantapp.dto.GrantCallDTO
import pt.unl.fct.grantapp.model.EvaluationPanelRepository
import pt.unl.fct.grantapp.model.SponsorEntityRepository
import pt.unl.fct.grantapp.model.StudentRepository
import pt.unl.fct.grantapp.model.dao.EvaluationPanelDAO
import pt.unl.fct.grantapp.model.dao.GrantCallDAO
import pt.unl.fct.grantapp.model.dao.SponsorEntityDAO
import pt.unl.fct.grantapp.model.dao.StudentDAO
import pt.unl.fct.grantapp.service.GrantCallService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var sponsors: SponsorEntityRepository

    @Autowired
    lateinit var evals: EvaluationPanelRepository

    @Autowired
    lateinit var students: StudentRepository

    @MockBean
    lateinit var grants: GrantCallService

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val grantsURL = "/grantcall"
    }

    @Test
    fun `Test create grant (NO ROLE)`() {
        val grantDTO = GrantCallDTO(1,"Grant1","Desc","requis",9999,Date(),Date(),1,1,0)
        val json = mapper.writeValueAsString(grantDTO)

        mvc.perform(post("$grantsURL")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andExpect(status().isUnauthorized)
                .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
    fun `Test create grant (WRONG ROLE - STUDENT)`() {
        val grantDTO = GrantCallDTO(1,"Grant1","Desc","requis",9999, Date(),Date(),1,1,0)
        val json = mapper.writeValueAsString(grantDTO)

        mvc.perform(post("$grantsURL")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Test
    @Transactional
    @WithMockUser(username = "SPONSOR",password = "SecretStuff",roles = ["SPONSOR"])
    fun `Test POST (RIGHT ROLE)`() {
        val sponsor = sponsors.save(SponsorEntityDAO())
        val eval = evals.save(EvaluationPanelDAO())

        val grant = GrantCallDTO(0, "title","desc","reqs",999,
                Date(),Date(), sponsor.id, eval.id,0)
        val grantDAO = GrantCallDAO(grant, sponsor, emptyList(), eval)

        val grantJSON = mapper.writeValueAsString(grant)

        Mockito.`when`(grants.addOne(nonNullAny(GrantCallDAO::class.java)))
                .then { assertEquals(it.getArgument(0), grantDAO); it.getArgument(0) }

        mvc.perform(post(grantsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantJSON))
                .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "SPONSOR 1",password = "SuperPassword",roles = ["SPONSOR"])
    fun `Test get grants (ROLE SPONSOR)`() {
        Mockito.`when`(grants.getAll()).thenReturn(mutableListOf())

        mvc.perform(MockMvcRequestBuilders.get(grantsURL))
                .andExpect(status().isOk())
    }




    @Test
    fun `Test sponsor editing another sponsor grant (Not owned)`() {

    }

    @Test
    fun `Test Reviewer have rights to update Application state`() {
        // TODO - Must be the chair of the eval panel!
    }


}