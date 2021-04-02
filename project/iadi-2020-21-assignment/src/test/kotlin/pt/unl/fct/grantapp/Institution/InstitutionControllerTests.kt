package pt.unl.fct.grantapp.StudentTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.Assert.assertThat
import pt.unl.fct.grantapp.service.InstitutionService
import pt.unl.fct.grantapp.dto.InstitutionDTO
import pt.unl.fct.grantapp.model.InstitutionRepository


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
//@WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
class InstitutionControllerTests {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var institutions: InstitutionService

    @Autowired
    lateinit var institutionRepository: InstitutionRepository

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val institution1 = InstitutionDAO(1L,"name","password","ROLE_INSTITUTION","email","address","city",emptyList(), emptyList())
        val institution2 = InstitutionDAO(2L,"name","password","ROLE_INSTITUTION","email","address","city",emptyList(), emptyList())


        val institutionsDAO = mutableListOf(institution1, institution2)

        val institutionsDTO = institutionsDAO.map { InstitutionDTO(it) }

        val institutionURL = "/institution"
    }

    @Test
    @WithMockUser(username = "INSTITUTION 1",password = "SuperPassword",roles = ["INSTITUTION"])
    fun `Test GET all sinstitutionsDAO` () {
        Mockito.`when`(institutions.getAll()).thenReturn(institutionsDAO)

        val result = mvc.perform(get("$institutionURL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(institutionsDAO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<InstitutionDTO>>(responseString)
        assertThat(responseDTO, equalTo(institutionsDTO as List<InstitutionDTO>))
    }

    @Test
    @WithMockUser(username = "INSTITUTION 1",password = "SuperPassword",roles = ["INSTITUTION"])
    fun `Test GET all institutions` () {
        Mockito.`when`(institutions.getAll()).thenReturn(institutionsDAO)

        val result = mvc.perform(get("$institutionURL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<InstitutionDTO>>(responseString)
        assertThat(responseDTO, equalTo(institutionsDTO))
    }

}