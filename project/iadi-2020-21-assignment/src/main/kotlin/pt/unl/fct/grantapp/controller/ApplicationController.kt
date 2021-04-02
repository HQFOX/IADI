package pt.unl.fct.grantapp.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.*
import pt.unl.fct.grantapp.model.dao.AppDataItemDAO
import pt.unl.fct.grantapp.model.dao.ApplicationDAO
import pt.unl.fct.grantapp.service.ApplicationService
import pt.unl.fct.grantapp.service.DataItemService
import pt.unl.fct.grantapp.service.GrantCallService
import pt.unl.fct.grantapp.service.StudentService

@RestController
@RequestMapping("/application")
class ApplicationController(
        val applicationService: ApplicationService,
        val grantService: GrantCallService,
        val studentService: StudentService,
        val dataItemsService: DataItemService) {

    @ApiOperation(value = "Get all applications", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully obtain the list of applications"),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @GetMapping("")
    fun getAllApplications(): Iterable<ApplicationDTO> = applicationService.getAll().map { ApplicationDTO(it) }

    @ApiOperation(value = "Get one application", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully obtain the one application"),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "No application found.")
    ])
    @GetMapping("/{id}")
    fun getOneApplication(@PathVariable id: Long) = ApplicationDTO(applicationService.getOne(id))

    @ApiOperation(value = "Get one application", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully obtain the one application"),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "No application found.")
    ])
    @GetMapping("/{id}/student")
    fun getOneApplicationStudent(@PathVariable id: Long) = StudentDTO(applicationService.getOne(id).student)

    @ApiOperation(value = "Get one application", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully obtain the one application"),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "No application found.")
    ])
    @GetMapping("/{id}/reviews")
    fun getOneApplicationReviews(@PathVariable id: Long) = applicationService.getOne(id).reviews.map { ReviewDTO(it) }


    @ApiOperation(value = "Get application data items", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully obtain the list of data items."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "No application found.")
    ])
    @GetMapping("/{appId}/dataitems")
    fun getApplicationDataItems(@PathVariable appId: Long): Iterable<AppDataItemDTO> =
            dataItemsService.getAllAppDataItemsByApplication(appId).map { AppDataItemDTO(it) }


    @ApiOperation(value = "Delete one application")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted application "),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "No application found.")
    ])
    @PreAuthorize("hasRole('STUDENT') and @securityService.canModifyApplication(principal, #appId)")
    @DeleteMapping("/{appId}")
    fun deleteApplication(@PathVariable appId: Long) = applicationService.deleteApplication(appId)

    @ApiOperation(value = "Create one application")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted application."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PostMapping("")
    @PreAuthorize("hasRole('STUDENT')")
    fun createApplication(@RequestBody application: StudentApplicationDTO) {
        val grant = grantService.getOne(application.grantId)
        val student = studentService.getOne(application.studentId)
        val applicationSubmited = applicationService.addOne(ApplicationDAO(application.application, grant, student))
        // Add Data Items
        val dataItems = application.dataItems.map { AppDataItemDAO(it, applicationSubmited, dataItemsService.getOne(it.dataItemId)) }
        dataItemsService.addAppDataItems(dataItems)
    }

    @ApiOperation(value = "Update application status (Approved/Denied)")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated application status."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    //@PreAuthorize("hasRole('REVIEWER') and @securityService.canUpdateApplicationStatus(principal, #id)")
    @PutMapping("/{id}/status")
    fun updateApplicationStatus(@PathVariable id: Long, @RequestBody applicationDTO: ApplicationDTO) {
        applicationService.updateApplicationStatus(applicationDTO.status, id)
    }

    @ApiOperation(value = "Update application status")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated application."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PreAuthorize("hasRole('STUDENT') and @securityService.canModifyApplication(principal, #appId)")
    @PutMapping("/{appId}")
    fun updateApplication(@PathVariable appId: Long, @RequestBody application: StudentApplicationDTO) = applicationService.updateApplication(appId, ApplicationDAO(application.application, grantService.getOne(application.grantId), studentService.getOne(application.studentId)))
}
