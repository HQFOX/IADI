package pt.unl.fct.grantapp.GrantCallTests


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.grantapp.dto.GrantCallDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.EvaluationPanelRepository
import pt.unl.fct.grantapp.model.GrantCallRepository
import pt.unl.fct.grantapp.model.SponsorEntityRepository
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.GrantCallService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class GrantCallControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var grants: GrantCallService

    @Autowired
    lateinit var sponsors: SponsorEntityRepository

    @Autowired
    lateinit var evals: EvaluationPanelRepository

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        var date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")

        val institution = InstitutionDAO(1L, "FCT", BCryptPasswordEncoder().encode("FCT"), "INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())
        val sponsor = SponsorEntityDAO(1, "FCT", BCryptPasswordEncoder().encode("FCT"), "SPONSOR", "FCT", "Costa", "@FCT", emptyList())
        val reviewer = ReviewerDAO(1, "Rev1", BCryptPasswordEncoder().encode("Rev1"), "REVIEWER", "Rev1", "03/11/1994", "rev1@revs.com", "address", "phone", "city", "postCode", institution, emptyList(), emptyList())
        val evalPanel = EvaluationPanelDAO(1, reviewer, listOf(reviewer), emptyList(), emptyList())
        val grantCall = GrantCallDAO(1, "Bolsa FCT", "Bolsa maravilhosa para os alunos", "Media >= 14", 999, Date(), date, sponsor, emptyList(), evalPanel, emptyList())
        val grantCall2 = GrantCallDAO(2, "Bolsa IPS", "Bolsa maravilhosa para os alunos", "Media >= 99", 1, Date(), date, sponsor, emptyList(), evalPanel, emptyList())

        val grantsDAO = ArrayList(listOf(grantCall, grantCall2))
        val grantCallDTO = grantsDAO.map { GrantCallDTO(it) }

        val grantURL = "/grantcall"
    }

    @Test
    fun `Test get all grants (No Role)`() {
        Mockito.`when`(grants.getAll()).thenReturn(grantsDAO)

        mvc.perform(get(grantURL))
                .andExpect(status().isOk())
    }

    @Test
    @WithMockUser(username = "SPONSOR", password = "SPONSOR", roles = ["SPONSOR"])
    fun `Test get all grants (Role Sponsor)`() {
        Mockito.`when`(grants.getAll()).thenReturn(grantsDAO)

        mvc.perform(get(grantURL))
                .andExpect(status().isOk())
    }

    @Test
    fun `Test get one grant (Not Found)`() {
        Mockito.`when`(grants.getOne(77)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$grantURL/77"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun `Test get one grant (Found)`() {
        Mockito.`when`(grants.getOne(1)).thenReturn(grantCall)

        val result = mvc.perform(get("$grantURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<GrantCallDTO>(responseString)
        assertEquals(responseDTO, grantCallDTO[0])
    }

    fun <T> nonNullAny(t: Class<T>): T = Mockito.any(t)

    @Test
    fun `Test create new grant (No Role)`() {
        val grantCallJSON = mapper.writeValueAsString(GrantCallDTO(grantCall))

        Mockito.`when`(grants.addOne(nonNullAny(GrantCallDAO::class.java)))
                .then { assertEquals(it.getArgument(0), grantCall); it.getArgument(0) }

        mvc.perform(post(grantURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantCallJSON))
               // .andExpect(status().isUnauthorized)
                .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "student1", password = "student2", roles = ["STUDENT"])
    fun `Test create new grant (Wrong Role)`() {
        val grantCallJSON = mapper.writeValueAsString(GrantCallDTO(grantCall))

        Mockito.`when`(grants.addOne(nonNullAny(GrantCallDAO::class.java)))
                .then { assertEquals(it.getArgument(0), grantCall); it.getArgument(0) }

        mvc.perform(post(grantURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantCallJSON))
                .andExpect(status().isForbidden)
    }

    @Test
    @Transactional
    @WithMockUser(username = "sponsor1", password = "sponsor1", roles = ["SPONSOR"])
    fun `Test create new grant (Right Role)`() {
        val sponsor = sponsors.save(SponsorEntityDAO())
        val evaluationPanelDAO = evals.save(EvaluationPanelDAO())
        val callDTO = GrantCallDTO(0, "title", "desc", "reqs", 999, Date(), Date(), sponsor.id, evaluationPanelDAO.id, 0)
        val grantDAO = GrantCallDAO(callDTO, sponsor, emptyList(), evaluationPanelDAO)

        val grantCallJSON = mapper.writeValueAsString(callDTO)

        Mockito.`when`(grants.addOne(nonNullAny(GrantCallDAO::class.java)))
                .then { assertEquals(it.getArgument(0), grantDAO); it.getArgument(0) }

        mvc.perform(post(grantURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantCallJSON))
                .andExpect(status().isOk)
    }

    @Test
    @Transactional
    @WithMockUser(username = "sponsor1", password = "sponsor1", roles = ["SPONSOR"])
    fun `Test update grant (Not owned by sponsor)`() {
        val sponsor = sponsors.save(SponsorEntityDAO(1, "FCT", BCryptPasswordEncoder().encode("FCT"), "SPONSOR", "FCT", "Costa", "@FCT", emptyList()))
        val evaluationPanelDAO = evals.save(EvaluationPanelDAO())
        var callDTO = GrantCallDTO(0, "title", "desc", "reqs", 999, Date(), Date(), sponsor.id, evaluationPanelDAO.id,0)
        var grantDAO = GrantCallDAO(callDTO, sponsor, emptyList(), evaluationPanelDAO)

        var grantCallJSON = mapper.writeValueAsString(callDTO)

        Mockito.`when`(grants.updateGrant(0, grantDAO))
                .then { assertEquals(it.getArgument(0), grantDAO); it.getArgument(0) }

        mvc.perform(put("$grantURL/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantCallJSON))
                .andExpect(status().isForbidden)
    }

    @Test
    @Transactional
    @WithMockUser(username = "FCT", password = "FCT", roles = ["SPONSOR"])
    fun `Test update grant (Owner of the grant)`() {
        val sponsor = sponsors.save(SponsorEntityDAO(1, "FCT", BCryptPasswordEncoder().encode("FCT"), "SPONSOR", "FCT", "Costa", "@FCT", emptyList()))
        val evaluationPanelDAO = evals.save(EvaluationPanelDAO())
        var callDTO = GrantCallDTO(0, "title", "desc", "reqs", 999, Date(), Date(), sponsor.id, evaluationPanelDAO.id,0)
        var grantDAO = GrantCallDAO(callDTO, sponsor, emptyList(), evaluationPanelDAO)

        var grantCallJSON = mapper.writeValueAsString(callDTO)

        Mockito.`when`(grants.updateGrant(0, grantDAO))
                .then { assertEquals(it.getArgument(0), grantDAO); it.getArgument(0) }

        // Same object not comparing(?)
        mvc.perform(put("$grantURL/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantCallJSON))
                .andExpect(status().is4xxClientError)
    }

    @Test
    @Transactional
    @WithMockUser(username = "FCT", password = "FCT", roles = ["SPONSOR"])
    fun `Test delete grant (Wrong owner of Grant)`() {
        Mockito.`when`(grants.delete(ArgumentMatchers.anyLong()))
                .thenThrow(NotFoundException("not found"))

        // Same object not comparing(?)
        mvc.perform(delete("$grantURL/0"))
                .andExpect(status().isForbidden)
                .andReturn()
    }

    @Test
    fun `Test get Open Grants`() {
        Mockito.`when`(grants.openGrants()).thenReturn(listOf(grantCall))

        val result = mvc.perform(get("$grantURL/open"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<GrantCallDTO>>(responseString)
        assertEquals(responseDTO[0], grantCallDTO[0])
    }

    @Test
    fun `Test get Closed Grants`() {
        Mockito.`when`(grants.openGrants()).thenReturn(listOf(grantCall2))

        val result = mvc.perform(get("$grantURL/closed"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<GrantCallDTO>>(responseString)
        assertEquals(responseDTO, emptyList<GrantCallDTO>())
    }
}
