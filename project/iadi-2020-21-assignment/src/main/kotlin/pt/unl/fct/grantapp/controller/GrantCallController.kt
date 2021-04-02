package pt.unl.fct.grantapp.controller


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.*
import pt.unl.fct.grantapp.model.dao.GrantCallDAO
import pt.unl.fct.grantapp.service.ApplicationService
import pt.unl.fct.grantapp.service.EvaluationPanelService
import pt.unl.fct.grantapp.service.GrantCallService
import pt.unl.fct.grantapp.service.SponsorEntityService
import java.util.*

@Api(value = "Grant Application System", description = "Management operations of Grant Calls")
@RestController
@RequestMapping("/grantcall")
class GrantCallController(
        val grantCallService: GrantCallService,
        val sponsorEntityService: SponsorEntityService,
        val evaluationPanelService: EvaluationPanelService
) {
    @ApiOperation(value = "Get a list of grants available", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @GetMapping("")
    fun getAllGrants(): List<GrantCallDTO> = grantCallService.getAllWithApplications().map {GrantCallDTO(it)}

    @ApiOperation(value = "Get a one grant from the list of grants available", response = GrantCallDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the grant"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @GetMapping("/{id}")
    fun getOneGrant(@PathVariable id: Long) = GrantCallDTO(grantCallService.getOne(id))

    @PreAuthorize("hasRole('SPONSOR')")
    @ApiOperation(value = "Create a new grant", response = GrantCallDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully created the grant"),
        ApiResponse(code = 401, message = "You are not authorized to create grants"),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PostMapping("")
    fun createNewGrant(@RequestBody grant: GrantCallDTO): GrantCallDTO =
            GrantCallDTO(grantCallService.addOne(GrantCallDAO(grant, sponsorEntityService.getOne(grant.sponsorId), emptyList(), evaluationPanelService.getOne(grant.evalPanel))))

    @PreAuthorize("hasRole('SPONSOR') and @securityService.canEditGrant(principal, #id)")
    @ApiOperation(value = "Update a existent grant", response = GrantCallDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated the grant"),
        ApiResponse(code = 401, message = "You are not authorized to update this grant"),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PutMapping("/{id}")
    fun updateGrant(@PathVariable id: Long, @RequestBody grant: GrantCallDTO) =
            GrantCallDTO(grantCallService.updateGrant(id, GrantCallDAO(grant, sponsorEntityService.getOne(grant.sponsorId), emptyList(), evaluationPanelService.getOne(grant.evalPanel))))

    @ApiOperation(value = "Delete a existent grant", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted the grant"),
        ApiResponse(code = 401, message = "You are not authorized to delete this grant"),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PreAuthorize("hasRole('SPONSOR') and @securityService.canEditGrant(principal, #id)")
    @DeleteMapping("/{id}")
    fun deleteGrant(@PathVariable id: Long) = grantCallService.delete(id)

    @ApiOperation(value = "Get a list of grants that are currently open for application", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of grants")
    ])
    @GetMapping("/open")
    fun getOpenGrants() = grantCallService.openGrants().map { GrantCallDTO(it) }

    @ApiOperation(value = "Get a list of grants that are closed for application", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of grants")
    ])
    @GetMapping("/closed")
    fun getClosedGrants() = grantCallService.closedGrants().map { GrantCallDTO(it) }

    @ApiOperation(value = "Get a list of grants that are opening in the next seven days", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of grants")
    ])
    @GetMapping("/openingSoon")
    fun getGrantsOpeningSoon() = grantCallService.openingSoon().map { GrantCallDTO(it) }

    @ApiOperation(value = "Get a list of grants from date to date", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of grants")
    ])
    @GetMapping("/date")
    fun getGrantsBetweenDate(@RequestParam(value = "date") @DateTimeFormat(pattern = "dd.MM.yyyy") date: Date) = grantCallService.getAllByDate(date).map { GrantCallDTO(it) }

    @ApiOperation(value = "Get a list of grants at date, funding and state", response = List::class)
    @GetMapping("/filter")
    fun getGrantsBetweenDate(@RequestParam(value = "date") @DateTimeFormat(pattern = "dd.MM.yyyy") date: Date,
                             @RequestParam(value = "minimum") minimum: Long,
                             @RequestParam(value = "maximum") maximum: Long)
            = grantCallService.getAllFiltered(date, minimum, maximum).map { GrantCallDTO(it) }

    @ApiOperation(value = "Get a list of applications submitted to a grant", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of applications")
    ])
    //@PreAuthorize("hasRole('SPONSOR') and @securityService.isOwnerOfGrant(principal, #id)")
    @GetMapping("/{id}/applications")
    fun getGrantApplications(@PathVariable id: Long) = grantCallService.getAllApplications(id).map { ApplicationDTO(it) }

    @ApiOperation(value = "Get a list of students that submitted a application to a grant", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of grants")
    ])
    @PreAuthorize("hasRole('SPONSOR') and @securityService.isOwnerOfGrant(principal, #id)")
    @GetMapping("/{id}/students")
    fun getGrantApplicationStudents(@PathVariable id: Long) = grantCallService.getAllStudents(id).map { StudentDTO(it) }

    @ApiOperation(value = "Get the current evaluation panel of the grant", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the evaluation panel"),
        ApiResponse(code = 404, message = "Evaluation not found/defined.")
    ])
    @PreAuthorize("hasRole('SPONSOR') and @securityService.isOwnerOfGrant(principal, #id)")
    @GetMapping("/{id}/evaluationpanel")
    fun getGrantEvaluationPanel(@PathVariable id: Long) = EvaluationPanelDTO(grantCallService.getAllEvaluationPanel(id))

    @ApiOperation(value = "Get specific grant data items")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved all dataitems."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    //@PreAuthorize("hasAnyRole('STUDENT','SPONSOR')")
    @GetMapping("/{id}/dataitems")
    fun getDataItems(@PathVariable id: Long) = grantCallService.getAllDataItems(id).map { DataItemDTO(it) }
}