package pt.unl.fct.grantapp.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.EvaluationPanelDTO
import pt.unl.fct.grantapp.dto.EvaluationPanelGrantsDTO
import pt.unl.fct.grantapp.dto.ReviewDTO
import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.model.dao.EvaluationPanelDAO
import pt.unl.fct.grantapp.model.dao.ReviewerDAO
import pt.unl.fct.grantapp.service.EvaluationPanelService
import pt.unl.fct.grantapp.service.GrantCallService
import pt.unl.fct.grantapp.service.InstitutionService
import pt.unl.fct.grantapp.service.ReviewerService

@Api(value = "Grant Application System", description = "Management operations of Evaluation Panel")
@RestController
@RequestMapping("/panel")
class EvaluationPanelController(
        val evaluationPanelService: EvaluationPanelService,
        val grantCallService: GrantCallService,
        val reviewerService: ReviewerService,
        val institutionService: InstitutionService
) {

    @ApiOperation(value = "View List of Evaluation Panels")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list of Evaluations Panels"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllEvaluationPanels() = evaluationPanelService.getAll().map { EvaluationPanelDTO(it) }

    @ApiOperation(value = "View a single Evaluation Panel By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved Evaluation Panel details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/{id}")
    fun getOneEvaluationPanel(@PathVariable id: Long) =
            EvaluationPanelDTO(evaluationPanelService.getOne(id))

    @ApiOperation(value = "Add a new Grant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a Evaluation Panel"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun createNewEvaluationPanel(@RequestBody panel: EvaluationPanelDTO) =
            evaluationPanelService.addOne(
                    EvaluationPanelDAO(panel, reviewerService.getOne(panel.chairID)))

    @ApiOperation(value = "Edit Evaluation Panel Details By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully edited Evaluation Panel details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("")
    fun editEvaluationPanel(@RequestBody panel: EvaluationPanelDTO) {
        evaluationPanelService.update(EvaluationPanelDAO(panel, reviewerService.getOne(panel.chairID)))
    }

    @ApiOperation(value = "Delete Evaluation Panel By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a Evaluation Panel"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deleteEvaluationPanel(@PathVariable id: Long) {
        evaluationPanelService.delete(id)
    }

    @GetMapping("/{id}/chair")
    fun getPanelChair(@PathVariable id: Long) = ReviewerDTO(evaluationPanelService.getPanelChair(id))

    //get panels of reviewer id
    @GetMapping("/chair/{id}")
    fun getPanelsOfChair(@PathVariable id: Long) =  evaluationPanelService.getPanelsOfChair(reviewerService.getOne(id).id).map { EvaluationPanelGrantsDTO(it) }

    //for some reason PUT does not work with nested objects
    @PostMapping("/{id}/chair")
    fun updatePanelChair(@PathVariable id: Long, @RequestBody panelChair: ReviewerDTO) = evaluationPanelService.updatePanelChair(id, ReviewerDAO(panelChair, institutionService.getOne(panelChair.institutionId)))

    //GET all members of evaluation panel
    @GetMapping("/{id}/members")
    fun getAllMembers(@PathVariable id: Long) = evaluationPanelService.getAllMembers(id).map { ReviewerDTO(it) }

    //GET all reviews of evaluation panel
    @GetMapping("/{id}/reviews")
    fun getAllReviews(@PathVariable id: Long) = evaluationPanelService.getAllReviews(id).map { ReviewDTO(it) }

    //POST member to evaluation panel
    @PostMapping("/{id}/members")

    fun addNewMember(@PathVariable id:Long, @RequestBody member: ReviewerDTO) = evaluationPanelService.addOneMember(id,reviewerService.getOne(member.reviewerId)).let { EvaluationPanelDTO(it) }

    //DELETE member of evaluation panel
    @DeleteMapping("{panelId}/members/{reviewerId}")
    fun deleteMember(@PathVariable panelId: Long, @PathVariable reviewerId: Long) = evaluationPanelService.deleteOneMember(panelId, reviewerService.getOne(reviewerId))
}