package pt.unl.fct.grantapp.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.ApplicationDTO
import pt.unl.fct.grantapp.dto.MyApplicationDTO
import pt.unl.fct.grantapp.dto.StudentDTO
import pt.unl.fct.grantapp.model.dao.StudentDAO
import pt.unl.fct.grantapp.service.InstitutionService
import pt.unl.fct.grantapp.service.StudentService

@Api(value = "Grant Application System", description = "Management operations of Student")
@RestController
@RequestMapping("/student")
class StudentController(
        var studentsService: StudentService,
        var institutionService: InstitutionService
) {

    @ApiOperation(value = "View List of Students")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list of Students"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @GetMapping("")
    fun getAllStudents() = studentsService.getAll().map { StudentDTO(it) }

    @ApiOperation(value = "View a single Student By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved Student details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/{id}")
    fun getOneStudent(@PathVariable id: Long) = StudentDTO(studentsService.getOne(id))

    //@PreAuthorize("hasAnyRoles('INSTITUTION') and @securityService.canAddStudent(principal)")
    @ApiOperation(value = "Add a new Student")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a Student"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun createNewStudent(@RequestBody student: StudentDTO) {
        studentsService.addOne(StudentDAO(student, institutionService.getOne(student.institutionID)))
    }

    @PreAuthorize("hasAnyRole('STUDENT','INSTITUTION') and @securityService.canEditStudent(principal, #id)")
    @ApiOperation(value = "Edit a Student By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully edited Student details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @PutMapping("/{id}")
    fun editStudent(@PathVariable id: Long, @RequestBody student: StudentDTO) =
            studentsService.updateStudent(id, StudentDAO(
                    student, institutionService.getOne(student.institutionID)

            ))

    @PreAuthorize("hasAnyRole('STUDENT','INSTITUTION') and @securityService.canEditStudent(principal, #id)")
    @ApiOperation(value = "Delete a Student By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a Student"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable id: Long) = studentsService.deleteStudent(id)

    @ApiOperation(value = "View Student apps By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved Student applications"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    //@PreAuthorize("hasAnyRole('STUDENT','INSTITUTION')")
    @GetMapping("/{studentId}/application")
    fun getStudentGrantApp(@PathVariable studentId: Long) = studentsService.getApplications(studentId).map { MyApplicationDTO(it) }

}