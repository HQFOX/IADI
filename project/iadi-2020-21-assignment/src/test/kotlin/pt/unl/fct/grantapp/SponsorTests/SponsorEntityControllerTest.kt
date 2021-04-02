package pt.unl.fct.grantapp.SponsorTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.junit.Assert.assertThat
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.grantapp.dto.SponsorEntityDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.SponsorEntityRepository
import pt.unl.fct.grantapp.model.dao.SponsorEntityDAO
import pt.unl.fct.grantapp.service.SponsorEntityService

@SpringBootTest
@AutoConfigureMockMvc
class SponsorEntityControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var sponsors:SponsorEntityService

    @MockBean
    lateinit var sponsorsRepo: SponsorEntityRepository

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val sponsor1 = SponsorEntityDAO(1L,"","","ROLE_SPONSOR","FCT","Costa da Caparica","support@fct.unl.pt", emptyList())
        val sponsor2 = SponsorEntityDAO(2L,"","","ROLE_SPONSOR","IPS","Setúbal","support@ips.pt",emptyList())
        val sponsorsDAO = mutableListOf(sponsor1, sponsor2)
        val sponsorsDTO = sponsorsDAO.map { SponsorEntityDTO(it) }

        val appURL = "/sponsor"
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `get all sponsors`() {
        Mockito.`when`(sponsors.getAll()).thenReturn(sponsorsDAO)

        mvc.perform(get("$appURL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andReturn()
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `get all sponsors DAO`() {
        Mockito.`when`(sponsors.getAll()).thenReturn(sponsorsDAO)

        mvc.perform(get("$appURL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(sponsorsDAO.size)))
                .andReturn()
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `get one sponsor`() {
        val sponsorID:Long = 1
        Mockito.`when`(sponsors.getOne(sponsorID)).thenReturn(sponsor1)

        mvc.perform(get("$appURL/$sponsorID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FCT"))
                .andReturn()
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Get one sponsor with invalid ID (Not Found)`() {
        Mockito.`when`(sponsors.getOne(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$appURL/2"))
                .andExpect(status().isNotFound())
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Create new sponsor`() {
        val newSponsorDAO = SponsorEntityDAO(0,"","","ROLE_SPONSOR","NewSponsor","New Street","New Contact", emptyList())
        val newSponsorDTO = SponsorEntityDTO(newSponsorDAO)

        val newSponsorJSON = mapper.writeValueAsString(newSponsorDTO)

        Mockito.`when`(sponsors.addOne(nonNullAny(SponsorEntityDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(newSponsorDAO)); it.getArgument(0) }

        mvc.perform(post("$appURL")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newSponsorJSON))
                .andExpect(status().isOk)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)



}