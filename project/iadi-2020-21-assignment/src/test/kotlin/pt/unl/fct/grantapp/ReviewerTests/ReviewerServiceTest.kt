package pt.unl.fct.grantapp.ReviewerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.mockito.ArgumentMatchers.anyLong
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import pt.unl.fct.grantapp.EvaluationPanelTests.EvaluationPanelServiceTest
import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.ReviewerRepository
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import pt.unl.fct.grantapp.model.dao.ReviewerDAO
import pt.unl.fct.grantapp.service.ReviewerService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ReviewerServiceTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var reviewerService: ReviewerService

    @MockBean
    lateinit var reviewerRepo: ReviewerRepository

    companion object {

        val institution = InstitutionDAO(1L,"FCT", BCryptPasswordEncoder().encode("FCT"),"INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())

        val reviewer1 = ReviewerDAO(2L,"","","ROLE","Jose","20-20-2020","jose@gmail.com","rua 25 de abril","2777777","Evora","7000", InstitutionDAO(), emptyList(), emptyList())
        val reviewer2 = ReviewerDAO(3L,"","","ROLE","Antonio","20-20-2020","antonio@gmail.com","rua 25 de abril","2777777","Evora","7000", InstitutionDAO() , emptyList(), emptyList())

        val reviewersDAO = mutableListOf(reviewer1, reviewer2)

        val reviewersDTO = reviewersDAO.map { ReviewerDTO(it) }
    }

    @Test
    fun `get all reviewers`() {
        Mockito.`when`(reviewerRepo.findAll()).thenReturn(reviewersDAO);

        assertThat(reviewerService.getAll(), equalTo(reviewersDAO as List<ReviewerDAO>))
    }

    @Test
    fun `getOne reviewer`() {
        Mockito.`when`(reviewerRepo.findById(2L)).thenReturn(Optional.of(reviewer1));

        assertThat(reviewerService.getOne(2L), equalTo(reviewer1))
    }

    @Test(expected = NotFoundException::class)
    fun `get one reviewer not found exception`() {
        Mockito.`when`(reviewerRepo.findById(anyLong())).thenReturn(Optional.empty())

        reviewerService.getOne(0L)
    }

    @Test
    fun `add one reviewer`() {
        Mockito.`when`(reviewerRepo.findById(2L)).thenReturn(Optional.of(reviewer2));

        assertThat(reviewerService.getOne(2L), equalTo(reviewer2))

    }


    @Test
    fun `test on adding a new reviewer`(){
        
        Mockito.`when`(reviewerRepo.save(Mockito.any(ReviewerDAO::class.java)))
                .then {
                    val reviewer: ReviewerDAO = it.getArgument(0)
                    assertThat(reviewer.id, equalTo(0L))
                    assertThat(reviewer.username, equalTo(reviewer1.username))
                    assertThat(reviewer.password, equalTo(reviewer1.password))
                    assertThat(reviewer.role, equalTo(reviewer1.role))
                    assertThat(reviewer.name, equalTo(reviewer.name))
                    assertThat(reviewer.birthday, equalTo(reviewer1.birthday))
                    assertThat(reviewer.email, equalTo(reviewer1.email))
                    assertThat(reviewer.address, equalTo(reviewer1.address))
                    assertThat(reviewer.telephone, equalTo(reviewer1.telephone))
                    assertThat(reviewer.city, equalTo(reviewer1.city))
                    assertThat(reviewer.postCode, equalTo(reviewer1.postCode))
                    assertThat(reviewer.institution, equalTo(reviewer1.institution))
                    assertThat(reviewer.evaluationPanels, equalTo(reviewer1.evaluationPanels))
                    assertThat(reviewer.reviews, equalTo(reviewer1.reviews))
                    reviewer
                }
        reviewerService.addOne(ReviewerDAO(0, reviewer1.username, reviewer1.password, reviewer1.role, reviewer1.name, reviewer1.birthday, reviewer1.email, reviewer1.address, reviewer1.telephone, reviewer1.city, reviewer1.postCode, reviewer1.institution, reviewer1.evaluationPanels, reviewer1.reviews))
        
    }

}