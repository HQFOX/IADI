package pt.unl.fct.grantapp.dto

import pt.unl.fct.grantapp.model.dao.ReviewerDAO

data class ReviewerDTO(
        val reviewerId: Long,
        val username: String,
        val password: String,
        val name: String,
        val birthday: String,
        val email: String,
        val address: String,
        val telephone: String,
        val city: String,
        val postcode: String,
        val institutionId: Long
) {
    /**
    constructor() : this(1, "name", "birthday", "email", "address", "telephone",
    "city", "postcode", InstitutionDTO())
    constructor(reviewer: ReviewerDAO) : this(reviewer.id,reviewer.name,reviewer.birthday, reviewer.email, reviewer.address, reviewer.telephone , reviewer.city , reviewer.postCode, InstitutionDTO())
     **/

    constructor(reviewer: ReviewerDAO) : this(reviewer.id, reviewer.username, reviewer.password, reviewer.name, reviewer.birthday, reviewer.email, reviewer.address, reviewer.telephone, reviewer.city, reviewer.postCode, reviewer.institution.id)
}


data class ReviewerPanelsDTO(
    val reviewerId: Long,
    val username: String,
    val name: String,
    val birthday: String,
    val email: String,
    val address: String,
    val telephone: String,
    val city: String,
    val postcode: String,
    val institutionId: Long,
    val evaluationPanels: List<EvaluationPanelGrantsDTO>
) {
    /**
    constructor() : this(1, "name", "birthday", "email", "address", "telephone",
    "city", "postcode", InstitutionDTO())
    constructor(reviewer: ReviewerDAO) : this(reviewer.id,reviewer.name,reviewer.birthday, reviewer.email, reviewer.address, reviewer.telephone , reviewer.city , reviewer.postCode, InstitutionDTO())
     **/

    constructor(reviewer: ReviewerDAO) : this(reviewer.id, reviewer.username, reviewer.name, reviewer.birthday, reviewer.email, reviewer.address, reviewer.telephone, reviewer.city, reviewer.postCode, reviewer.institution.id, reviewer.evaluationPanels.map{ EvaluationPanelGrantsDTO(it)})
}
