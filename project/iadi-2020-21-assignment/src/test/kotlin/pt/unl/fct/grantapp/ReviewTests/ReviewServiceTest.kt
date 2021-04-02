package pt.unl.fct.grantapp.ReviewTests


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
import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.ReviewRepository
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.ReviewService

import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    lateinit var reviewService: ReviewService

    @MockBean
    lateinit var reviewRepo: ReviewRepository

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val institution = InstitutionDAO(1L,"FCT", BCryptPasswordEncoder().encode("FCT"),"INSTITUTION", "FCT", "monte da caparica", "fct@fct.com", emptyList(), emptyList())

        val reviewer1 = ReviewerDAO(2L,"","","ROLE","Jose","20-20-2020","jose@gmail.com","rua 25 de abril","2777777","Evora","7000", InstitutionDAO(), emptyList(), emptyList())
        val reviewer2 = ReviewerDAO(3L,"","","ROLE","Antonio","20-20-2020","antonio@gmail.com","rua 25 de abril","2777777","Evora","7000", InstitutionDAO(), emptyList(), emptyList())

        val reviewersDAO = listOf(reviewer1, reviewer2)

        val review1 = ReviewDAO(1, "Esta aplicacao e muito boa....", ApplicationDAO(), ReviewerDAO(), EvaluationPanelDAO())
        val review2 = ReviewDAO(2, "Nao acho assim tao boa...", ApplicationDAO(), ReviewerDAO(), EvaluationPanelDAO())

        val reviewsDAO = mutableListOf(review1, review2)

        val reviewersDTO = reviewersDAO.map { ReviewerDTO(it) }
    }

    @Test
    fun `get all reviews`() {
        Mockito.`when`(reviewRepo.findAll()).thenReturn(reviewsDAO);

        assertThat(reviewService.getAll(), equalTo(reviewsDAO))
    }

    @Test
    fun `getOne review`() {
        Mockito.`when`(reviewRepo.findById(1L)).thenReturn(Optional.of(review1));

        assertThat(reviewService.getOne(1L), equalTo(review1))
    }

    @Test(expected = NotFoundException::class)
    fun `get one review not found exception`() {
        Mockito.`when`(reviewRepo.findById(anyLong())).thenReturn(Optional.empty())

       // assertThrows<NotFoundException> { reviewService.getOne(10L) }
        reviewService.getOne(10L)
    }

    @Test
    fun `add one review`() {
        Mockito.`when`(reviewRepo.save(Mockito.any(ReviewDAO::class.java)))
                .then {
                    val reviewDAO:ReviewDAO = it.getArgument(0)
                    assertThat(reviewDAO.id, equalTo(0L))
                    assertThat(reviewDAO.writtenReview, equalTo(review1.writtenReview))
                    assertThat(reviewDAO.application, equalTo(review1.application))
                    assertThat(reviewDAO.reviewer, equalTo(review1.reviewer))
                    assertThat(reviewDAO.evaluationPanel, equalTo(review1.evaluationPanel))
                    reviewDAO
                }

        reviewService.addOne(ReviewDAO(0L, review1.writtenReview, review1.application, review1.reviewer, review1.evaluationPanel ))

    }


}