package pt.unl.fct.grantapp.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.ApplicationReviewDTO
import pt.unl.fct.grantapp.dto.ReviewDTO
import pt.unl.fct.grantapp.model.dao.ReviewDAO
import pt.unl.fct.grantapp.service.ApplicationService
import pt.unl.fct.grantapp.service.EvaluationPanelService
import pt.unl.fct.grantapp.service.ReviewService
import pt.unl.fct.grantapp.service.ReviewerService

@RestController
@RequestMapping("/review")
class ReviewController(
        val reviewService: ReviewService,
        val applicationService: ApplicationService,
        val reviewerService: ReviewerService,
        val evaluationPanelService: EvaluationPanelService
) {

    @ApiOperation(value = "View List of reviews")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list of reviews"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllReviews() = reviewService.getAll().map { ReviewDTO(it) }

    @ApiOperation(value = "Get one review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved one review"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @GetMapping("/{id}")
    fun getOneReview(@PathVariable id: Long) = ReviewDTO(reviewService.getOne(id))

    @ApiOperation(value = "Create new review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully created review"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    //@PreAuthorize("hasRole('REVIEWER') and @securityService.canAddReview(principal)")
    @PostMapping("")
    fun createNewReview(@RequestBody review: ReviewDTO) =
            reviewService.addOne(ReviewDAO(review, applicationService.getOne(review.applicationId), reviewerService.getOne(review.reviewerId), evaluationPanelService.getOne(review.evaluationPanelId))).let { ReviewDTO(it)}

    @ApiOperation(value = "Update review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated review"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun updateReview(@PathVariable id: Long, @RequestBody review: ReviewDTO) =
            ReviewDTO(reviewService.updateReview(id, ReviewDAO(review, applicationService.getOne(review.applicationId), reviewerService.getOne(review.reviewerId), evaluationPanelService.getOne(review.evaluationPanelId))))

    @ApiOperation(value = "Delete review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted review"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "Not Found resource")
    ])
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: Long) = reviewService.delete(id)

    @GetMapping("/app/{appId}")
    fun getAppReviews(@PathVariable appId: Long) = reviewService.getAllByAppId(appId).map { ApplicationReviewDTO(it) }
}