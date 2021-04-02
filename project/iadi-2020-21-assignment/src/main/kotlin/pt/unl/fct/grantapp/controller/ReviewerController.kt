package pt.unl.fct.grantapp.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.EvaluationPanelDTO
import pt.unl.fct.grantapp.dto.EvaluationPanelGrantsDTO
import pt.unl.fct.grantapp.dto.ReviewerDTO
import pt.unl.fct.grantapp.dto.ReviewerPanelsDTO
import pt.unl.fct.grantapp.model.dao.ReviewerDAO
import pt.unl.fct.grantapp.service.InstitutionService
import pt.unl.fct.grantapp.service.ReviewerService


@Api(value = "Grant Application System", description = "Management operations of Reviewer")
@RestController
@RequestMapping("/reviewer")
class ReviewerController(
        val reviewerService: ReviewerService,
        val institutionService: InstitutionService
) {

    // GET - Lista todos
    @ApiOperation(value = "View List of Reviewers")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list of Reviewers"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllReviewers() = reviewerService.getAll().map { ReviewerDTO(it) }


    @ApiOperation(value = "View a single Reviewer By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved Reviewer details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/{id}")
    fun getOneReviewer(@PathVariable id: Long) =
            ReviewerDTO(reviewerService.getOne(id))

    @ApiOperation(value = "View Reviewer Panels By Reviewer Id ")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved Reviewer details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/{id}/panels")
    fun getReviewerPanels(@PathVariable id: Long) =
        reviewerService.getOne(id).evaluationPanels.map { EvaluationPanelGrantsDTO(it) }


    @ApiOperation(value = "Add a new Reviewer")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a Reviewer"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun createNewReviewer(@RequestBody reviewer: ReviewerDTO) {
        reviewerService.addOne(ReviewerDAO(reviewer, institutionService.getOne(reviewer.institutionId)))
    }

    @ApiOperation(value = "Edit a Reviewer By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully edited Reviewer details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun editReviewer(@PathVariable id: Long, @RequestBody reviewer: ReviewerDTO) {
        reviewerService.updateReviewer(id,
                ReviewerDAO(reviewer, institutionService.getOne(reviewer.institutionId)
                )
        )
    }

    @ApiOperation(value = "Delete a Reviewer By Id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a Reviewer"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deleteReviewer(@PathVariable id: Long) {
        reviewerService.delete(id)
    }
}