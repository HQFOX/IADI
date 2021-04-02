package pt.unl.fct.grantapp.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.GrantCallDTO
import pt.unl.fct.grantapp.dto.SponsorEntityDTO
import pt.unl.fct.grantapp.model.dao.SponsorEntityDAO
import pt.unl.fct.grantapp.service.SponsorEntityService

@Api(value = "Grant Application System", description = "Management operations of Sponsor Entity")
@RestController
@RequestMapping("/sponsor")
class SponsorEntityController(
        val sponsorService: SponsorEntityService
) {

    @ApiOperation(value = "View List of Sponsors")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list of Sponsors"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllGrants() = sponsorService.getAll().map { SponsorEntityDTO(it) }

    @ApiOperation(value = "Get one sponsor by id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved sponsor by id"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @GetMapping("/{id}")
    fun getOneSponsor(@PathVariable id: Long) = SponsorEntityDTO(sponsorService.getOne(id))

    @ApiOperation(value = "Create new Sponsor")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully created sponsor"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun createNewSponsor(@RequestBody sponsor: SponsorEntityDTO): SponsorEntityDTO =
            SponsorEntityDTO(sponsorService.addOne(SponsorEntityDAO(sponsor)))

    @ApiOperation(value = "Update Sponsor")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated sponsor"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @PutMapping("/{id}")
    fun updateSponsor(@PathVariable id: Long, @RequestBody sponsor: SponsorEntityDTO) =
            sponsorService.updateSponsor(id, SponsorEntityDAO(sponsor))

    @ApiOperation(value = "Delete Sponsor")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted sponsor"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @DeleteMapping("/{id}")
    fun deleteSponsor(@PathVariable id: Long) = sponsorService.deleteSponsor(id)

    @ApiOperation(value = "Get sponsor grantcalls")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved sponsors grancalls"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/{id}/grantcall")
    fun getSponsorGrantCalls(@PathVariable id: Long): List<GrantCallDTO> =
            sponsorService.getGrantCalls(id).map { GrantCallDTO(it) }
}