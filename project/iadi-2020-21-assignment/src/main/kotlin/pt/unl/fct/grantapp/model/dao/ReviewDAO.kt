package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.ReviewDTO
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class ReviewDAO(
        @Id
        @GeneratedValue
        var id: Long,

        var writtenReview: String,

        @ManyToOne
        var application: ApplicationDAO,

        @ManyToOne
        var reviewer: ReviewerDAO,

        @ManyToOne
        var evaluationPanel: EvaluationPanelDAO
) {
    constructor(reviewDTO: ReviewDTO, application: ApplicationDAO, reviewer: ReviewerDAO, evaluationPanel: EvaluationPanelDAO) :
            this(reviewDTO.reviewerId, reviewDTO.writtenReview, application, reviewer, evaluationPanel)

    constructor() : this(0, "", ApplicationDAO(), ReviewerDAO(), EvaluationPanelDAO())

    fun update(reviewer: ReviewDAO) {
        TODO("Not yet implemented")
    }


}