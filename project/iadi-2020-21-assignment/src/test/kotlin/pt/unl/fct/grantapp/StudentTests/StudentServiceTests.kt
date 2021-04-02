package pt.unl.fct.grantapp.StudentTests

import pt.unl.fct.grantapp.exceptions.NotFoundException
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.grantapp.dto.StudentDTO
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import pt.unl.fct.grantapp.model.dao.StudentDAO
import pt.unl.fct.grantapp.service.StudentService
import org.junit.Assert.assertThat
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import pt.unl.fct.grantapp.model.StudentRepository
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentServiceTests {

    @Autowired
    lateinit var studentsService:StudentService

    @MockBean
    lateinit var studentRepository: StudentRepository

    companion object Constants {

        val institution = InstitutionDAO(1L,"name","birthday","ROLE_INSTITUTION","email","address","city",emptyList(), emptyList())

        val student1 = StudentDAO(2L, "", "", "ROLE_STUDENT",
                "Student1","03-02-1998", "suportp@fct.unl.pt", "address", "telephone", "city" , "postcode",
                institution, emptyList())
        val student2 = StudentDAO(3L, "username", "password", "ROLE_STUDENT",
                "Student2","birthday", "email", "address", "telephone", "city" , "postcode",
                institution, emptyList())

        val studentsDAO = mutableListOf(student1, student2)

        val studentsDTO = studentsDAO.map{ StudentDTO(it)}
    }

    @Test
    fun `GetAll test`() {
       Mockito.`when`(studentRepository.findAll()).thenReturn(studentsDAO)

        assertThat(studentsService.getAll(), equalTo((studentsDAO as Iterable<StudentDAO>)))
    }

    @Test
    fun `getOne student`() {
        Mockito.`when`(studentRepository.findById(2L)).thenReturn(Optional.of(student1));

        assertThat(studentsService.getOne(2L), equalTo(student1))
    }

    @Test
    fun `student addOne`() {
        Mockito.`when`(studentRepository.save(Mockito.any(StudentDAO::class.java)))
                .then {
                    val student:StudentDAO = it.getArgument(0)
                    assertEquals(student.id, student1.id)
                    assertEquals(student.name, student.name)
                    assertEquals(student.birthday, student.birthday)
                    assertEquals(student.email, student.email)
                    assertEquals(student.address, student.address)
                    assertEquals(student.telephone, student.telephone)
                    assertEquals(student.city, student.city)
                    assertEquals(student.postcode, student.postcode)
                    assertEquals(student.institution, student.institution)
                    student
                }

        studentsService.addOne(student1)

    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne student exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(studentRepository.findById(anyLong())).thenReturn(Optional.empty())
        studentsService.getOne(0L)
    }
    
    
    @Test()
    fun `Test DELETE student`() {
        studentRepository.deleteAll()
        assertEquals(studentRepository.findAll().toList().size, 0)
    }

    @Test(expected = NotFoundException::class)
    fun `Test getOne Non existent`(){
        Mockito.`when`(studentRepository.findById(anyLong())).thenReturn(Optional.empty())
        studentsService.getOne(999)
    }

}