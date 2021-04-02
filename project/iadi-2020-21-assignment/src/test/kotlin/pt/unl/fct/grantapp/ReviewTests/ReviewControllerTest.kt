package pt.unl.fct.grantapp.ReviewTests

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.junit.Assert.assertEquals
import pt.unl.fct.grantapp.dto.ReviewerDTO

import org.springframework.transaction.annotation.Transactional
import org.hamcrest.Matchers.hasSize
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import pt.unl.fct.grantapp.dto.ReviewDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.*
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.ReviewService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var instRepo: InstitutionRepository

    @Autowired
    lateinit var appRepo: ApplicationRepository

    @Autowired
    lateinit var reviewerRepo: ReviewerRepository

    @Autowired
    lateinit var sponsorRepo: SponsorEntityRepository

    @Autowired
    lateinit var grantRepo: GrantCallRepository


    @Autowired
    lateinit var panelRepo: EvaluationPanelRepository



    @MockBean
    lateinit var reviewService: ReviewService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6

        val mapper = ObjectMapper().registerModule(KotlinModule())

        val sponsor1 = SponsorEntityDAO(1,"FCT",BCryptPasswordEncoder().encode("FCT"),"SPONSOR","FCT","Costa","@FCT", emptyList())

        val institution = InstitutionDAO(1L,"FCT",BCryptPasswordEncoder().encode("FCT"),"INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())


        val reviewer1 = ReviewerDAO(2L,"","","ROLE","Jose","20-20-2020","jose@gmail.com","rua 25 de abril","2777777","Evora","7000", institution, emptyList(), emptyList())
        val reviewer2 = ReviewerDAO(3L,"","","ROLE","Antonio","20-20-2020","antonio@gmail.com","rua 25 de abril","2777777","Evora","7000", institution, emptyList(), emptyList())

        val evalPanel1 = EvaluationPanelDAO(1, reviewer1, emptyList(), emptyList(), emptyList())

        val student1 = StudentDAO(1, "stud1", BCryptPasswordEncoder().encode("stud1"), "STUDENT",
                "Student1","birthday", "email", "address", "telephone", "city" , "postcode",
                institution, emptyList())

        var date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")
        val grantCall1 = GrantCallDAO(1, "Bolsa FCT","Bolsa maravilhosa para os alunos", "Média >= 14", 999, Date(), date, sponsor1, emptyList(), evalPanel1, emptyList())



        val application1 = ApplicationDAO(1, "Very good intro", "Field work", "Sleep", "none", "Waiting Response", student1, grantCall1, emptyList(), emptyList())



        val review1 = ReviewDAO(1, "Esta aplicacao e muito boa....", application1, reviewer1, evalPanel1)
        val review2 = ReviewDAO(2, "Nao acho assim tao boa...", application1, reviewer2, evalPanel1)

        val reviewsDAO = ArrayList(listOf(review1, review2))

        val reviewsDTO = reviewsDAO.map { ReviewDTO(it) }
        val reviewURL = "/review"

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Get All Reviews`(){
        //MatcherAssert.assertThat(reviewerService.getAll(), Matchers.equalTo(emptyList()))
        Mockito.`when`(reviewService.getAll()).thenReturn(reviewsDAO)

        val result = mvc.perform(get(reviewURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(reviewsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<ReviewDTO>>(responseString)
        assertThat(responseDTO, equalTo(reviewsDTO))

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Get One Review`(){
        Mockito.`when`(reviewService.getOne(2L)).thenReturn(review2)

        val result = mvc.perform(get("$reviewURL/2"))
                .andExpect(status().isOk)
                .andReturn()
        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ReviewDTO>(responseString)
        assertThat(responseDTO, equalTo(reviewsDTO[1]))

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Get One Review (Not Found)`(){
        Mockito.`when`(reviewService.getOne(2L)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$reviewURL/2"))
                .andExpect(status().is4xxClientError)


    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Test
    @Transactional
    @WithMockUser(username = "rev1", password = "rev1", roles = ["REVIEWER"])
    fun `Test Post Review from diferent institutions`(){
        /**
        val sponsor = sponsorRepo.save(SponsorEntityDAO())
        val evaluationPanelDAO = panelRepo.save(EvaluationPanelDAO())
        val grant = grantRepo.save(GrantCallDAO())
        val app = appRepo.save(ApplicationDAO())
        val rev = reviewerRepo.save(ReviewerDAO())
        val reviewDTO = ReviewDTO(5,"written",app.id,rev.id,evaluationPanelDAO.id)

        val reviewDAO = ReviewDAO(reviewDTO,app,rev,evaluationPanelDAO)

        val grantCallJSON = mapper.writeValueAsString(reviewDTO)

        Mockito.`when`(reviewService.addOne(nonNullAny(ReviewDAO::class.java)))
                .then { assertEquals(it.getArgument(0), reviewDAO); it.getArgument(0) }

        mvc.perform(post(reviewURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(grantCallJSON))
                .andExpect(status().isOk)
        **/
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["CLIENT"])
    fun `Test Delete Review`(){


        val result = mvc.perform(delete("$reviewURL/2"))
                .andExpect(status().isOk)
                .andReturn()
    }
}