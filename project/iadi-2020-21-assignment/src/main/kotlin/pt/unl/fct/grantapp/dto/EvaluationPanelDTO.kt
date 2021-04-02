package pt.unl.fct.grantapp.dto

import pt.unl.fct.grantapp.model.dao.EvaluationPanelDAO

data class EvaluationPanelDTO(
        val evaluationPanelId: Long,
        val chairID: Long,
        val members: List<ReviewerDTO>
) {
    constructor(panel: EvaluationPanelDAO) : this(panel.id, panel.chair.id, panel.members.map { ReviewerDTO(it) })
}


data class EvaluationPanelGrantsDTO(
    val evaluationPanelId: Long,
    val chairID: Long,
    val members: List<ReviewerDTO>,
    val grants: List<GrantCallDTO>

) {
    constructor(panel: EvaluationPanelDAO) : this(panel.id, panel.chair.id, panel.members.map { ReviewerDTO(it) }, panel.grantCalls.map { GrantCallDTO(it) })
}