package pt.unl.fct.grantapp.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.InstitutionDTO
import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.dto.StudentDTO
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import pt.unl.fct.grantapp.service.InstitutionService

@Api(value = "Grant Application System", description = "Management operations of Institutions")
@RestController
@RequestMapping("/institution")
class InstitutionController(
        val institutionService: InstitutionService
) {

    // GET - Lista todos
    @ApiOperation(value = "View List of Institutions")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list of Grants"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllInstitutions() = institutionService.getAll().map { InstitutionDTO(it) }

    @ApiOperation(value = "View a single Institution By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved Institution details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/{id}")
    fun getOneInstitution(@PathVariable id: Long) =
            InstitutionDTO(institutionService.getOne(id))

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add a new Institution")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a Institution"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("/")
    fun createNewInstitution(@RequestBody institution: InstitutionDTO) {
        institutionService.addOne(
                InstitutionDAO(institution)
        )
    }

    @PreAuthorize("hasRole('INSTITUTION') and @securityService.canEditGrant(principal, #id)")
    @ApiOperation(value = "Edit a Institution By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully edited Institution details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun editInstitution(@PathVariable id: Long, @RequestBody institution: InstitutionDTO) {
        institutionService.updateInstitution(id,
                InstitutionDAO(
                        institution
                )
        )
    }

    @PreAuthorize("hasRole('INSTITUTION') and @securityService.canEditGrant(principal, #id)")
    @ApiOperation(value = "Delete a Institution By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a Institution"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deleteInstitution(@PathVariable id: Long) {
        institutionService.delete(id)
    }

    @GetMapping("/{instituionId}/students")
    fun getInstStudents(@PathVariable instituionId: Long) = institutionService.getStudents(instituionId).map { StudentDTO(it) }

    @GetMapping("/{instituionId}/reviewers")
    fun getInstReviewers(@PathVariable instituionId: Long) = institutionService.getReviewers(instituionId).map { ReviewerDTO(it) }

}