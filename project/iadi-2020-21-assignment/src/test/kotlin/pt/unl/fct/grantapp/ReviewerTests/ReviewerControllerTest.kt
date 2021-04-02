package pt.unl.fct.grantapp.ReviewerTests

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import pt.unl.fct.grantapp.model.dao.ReviewerDAO
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
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import pt.unl.fct.grantapp.exceptions.NotFoundException

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ReviewerControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var reviewerService: ReviewerService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6

        val mapper = ObjectMapper().registerModule(KotlinModule())

        val institution = InstitutionDAO(1L,"FCT", BCryptPasswordEncoder().encode("FCT"),"INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())

        val reviewer1 = ReviewerDAO(2L,"","","ROLE","Jose","20-20-2020","jose@gmail.com","rua 25 de abril","2777777","Evora","7000", institution, emptyList(), emptyList())
        val reviewer2 = ReviewerDAO(3L,"","","ROLE","Antonio","20-20-2020","antonio@gmail.com","rua 25 de abril","2777777","Evora","7000", institution, emptyList(), emptyList())

        val reviewersDAO = ArrayList(listOf(reviewer1, reviewer2))

        val reviewersDTO = reviewersDAO.map { ReviewerDTO(it) }
        val reviewersURL = "/reviewer"

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Get All Reviewers`(){
        //MatcherAssert.assertThat(reviewerService.getAll(), Matchers.equalTo(emptyList()))
        Mockito.`when`(reviewerService.getAll()).thenReturn(reviewersDAO)

        val result = mvc.perform(get(reviewersURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(reviewersDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<ReviewerDTO>>(responseString)
        assertThat(responseDTO, equalTo(reviewersDTO))

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Get One Reviewer`(){
        Mockito.`when`(reviewerService.getOne(2L)).thenReturn(reviewer2)

        val result = mvc.perform(get("$reviewersURL/2"))
                .andExpect(status().isOk)
                .andReturn()
        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ReviewerDTO>(responseString)
        assertThat(responseDTO, equalTo(reviewersDTO[1]))

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Get One Reviewer (Not Found)`(){
        Mockito.`when`(reviewerService.getOne(2L)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$reviewersURL/2"))
                .andExpect(status().is4xxClientError)


    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["REVIEWER"])
    fun `Test Post Reviewer`(){
        /**
        val newreviewer = ReviewerDTO(0L,"","","Antonio","20-20-2020","antonio@gmail.com","rua 25 de abril","2777777","Evora","7000", 1L)

        Mockito.`when`(reviewerService.addOne(ReviewerDAO(newreviewer, institution)))


        //val newReviewerDTO = ReviewerDAO(newreviewer)
        val newReviewerJSON = mapper.writeValueAsString(newreviewer)

        mvc.perform(post(reviewersURL)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(newReviewerJSON))
                 .andExpect(status().isOk)
        **/
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Delete Reviewer`(){


        val result = mvc.perform(delete("$reviewersURL/2"))
                .andExpect(status().isOk)
                .andReturn()
    }
}