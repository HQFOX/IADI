package pt.unl.fct.grantapp.dto

import pt.unl.fct.grantapp.model.dao.ReviewDAO

data class ReviewDTO(
        val reviewId: Long,
        val writtenReview: String,
        val applicationId: Long,
        val reviewerId: Long,
        val evaluationPanelId: Long

) {
    constructor(review: ReviewDAO) : this(review.id, review.writtenReview, review.application.id, review.reviewer.id, review.evaluationPanel.id)
}

data class ApplicationReviewDTO(
        val reviewId: Long,
        val writtenReview: String,
        val application: ApplicationDTO,
        val reviewer: ReviewerDTO
){
    constructor(review: ReviewDAO) : this(review.id, review.writtenReview, ApplicationDTO(review.application), ReviewerDTO(review.reviewer))
}