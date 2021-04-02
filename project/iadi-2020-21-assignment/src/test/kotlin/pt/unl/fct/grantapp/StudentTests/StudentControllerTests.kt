package pt.unl.fct.grantapp.StudentTests

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import pt.unl.fct.grantapp.exceptions.NotFoundException
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
import pt.unl.fct.grantapp.dto.StudentDTO
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import pt.unl.fct.grantapp.model.dao.StudentDAO
import pt.unl.fct.grantapp.service.StudentService
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.Assert.assertThat
import org.springframework.http.MediaType
import pt.unl.fct.grantapp.service.InstitutionService
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.grantapp.model.InstitutionRepository
import pt.unl.fct.grantapp.model.StudentRepository


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
//@WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
class StudentControllerTests {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var students: StudentService

    @Autowired
    lateinit var studentRepository: StudentRepository

    @MockBean
    lateinit var institutions: InstitutionService

    @Autowired
    lateinit var institutionRepository: InstitutionRepository



    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val institution = InstitutionDAO(1L,"name","birthday","ROLE_INSTITUTION","email","address","city",emptyList(), emptyList())


        val student1 = StudentDAO(1L, "", "", "ROLE_STUDENT",
                "Student1","03-02-1998", "suportp@fct.unl.pt", "address", "telephone", "city" , "postcode",
                institution, emptyList())
        val student2 = StudentDAO(2L, "username", "password", "ROLE_STUDENT",
                "Student2","birthday", "email", "address", "telephone", "city" , "postcode",
                institution, emptyList())

        val studentsDAO = mutableListOf(student1, student2)

        val studentsDTO = studentsDAO.map { StudentDTO(it) }

        val studentURL = "/student"
    }

    @Test
    @WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
    fun `Test GET all studentsDAO` () {
        Mockito.`when`(students.getAll()).thenReturn(studentsDAO)

        val result = mvc.perform(get("$studentURL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(studentsDAO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<StudentDTO>>(responseString)
        assertThat(responseDTO, equalTo(studentsDTO))
    }

    @Test
    @WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
    fun `Test GET all students` () {
        Mockito.`when`(students.getAll()).thenReturn(studentsDAO)

        val result = mvc.perform(get("$studentURL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<StudentDTO>>(responseString)
        assertThat(responseDTO, equalTo(studentsDTO))
    }


    @Test
    @WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
    fun `Test GET one Student`() {
        val studentID:Long = 1
        Mockito.`when`(students.getOne(studentID)).thenReturn(student1)

        val result = mvc.perform(get("$studentURL/$studentID"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Student1"))
                    .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<StudentDTO>(responseString)
        assertThat(responseDTO, equalTo(studentsDTO[0]))
    }


    @Test
    @WithMockUser(username = "STUDENT 1",password = "SuperPassword",roles = ["STUDENT"])
    fun `Get one student with invalid ID (Not Foud)`() {
        Mockito.`when`(students.getOne(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$studentURL/2"))
                .andExpect(status().isNotFound())
    }

}