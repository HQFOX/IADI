package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.EvaluationPanelDTO
import javax.persistence.*

@Entity(name = "EvaluationPanel")
data class EvaluationPanelDAO(
    @Id
        @GeneratedValue
        val id: Long,
    @ManyToOne
        var chair: ReviewerDAO,
    @ManyToMany
        var members: List<ReviewerDAO>,
    @OneToMany(mappedBy = "panel")
        var grantCalls: List<GrantCallDAO>,
    @ManyToMany
        val reviews: List<ReviewDAO>
) {
    constructor(panel: EvaluationPanelDTO, chair: ReviewerDAO) : this(panel.evaluationPanelId, chair, emptyList(), emptyList(), emptyList())
    constructor(panel: EvaluationPanelDTO, chair: ReviewerDAO, members: List<ReviewerDAO>) : this(panel.evaluationPanelId, chair, members, emptyList(), emptyList())
    constructor() : this(0, ReviewerDAO(), emptyList(), emptyList(), emptyList())

    fun updateChair(newChair: ReviewerDAO) {
        this.chair = newChair
    }

    fun addMember(newMember : ReviewerDAO) {
        if (!this.members.contains(newMember))
            this.members = this.members.plus(newMember)

    }

    fun removeMember(member: ReviewerDAO) {
            if (this.members.contains(member))
                this.members = this.members.minus(member)
    }
}