package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.ReviewerDTO
import javax.persistence.*

@Entity(name = "Reviewer")
data class ReviewerDAO(
        @Id
        @GeneratedValue
        override val id: Long,
        override var username: String,
        override var password: String,
        override var role: String,
        var name: String,
        var birthday: String,
        var email: String,
        var address: String,
        var telephone: String,
        var city: String,
        var postCode: String,

        @ManyToOne
        var institution: InstitutionDAO,

        @ManyToMany(mappedBy = "members")
        var evaluationPanels: List<EvaluationPanelDAO>,

        @ManyToMany
        var reviews: List<ReviewDAO>
) : UserDAO(id, username, password, role) {
    //constructor(id:Long, name: String, birthday: String, email: String, address: String, telephone: String, city: String)
    constructor(reviewer: ReviewerDTO, institution: InstitutionDAO) :
            this(reviewer.reviewerId, reviewer.username, reviewer.password, "ROLE_REVIEWER", reviewer.name, reviewer.birthday, reviewer.email, reviewer.address, reviewer.telephone, reviewer.city, reviewer.postcode, institution, emptyList(), emptyList())

    constructor() : this(0, "", "", "", "", "", "", "", "", "", "", InstitutionDAO(), emptyList<EvaluationPanelDAO>(), emptyList<ReviewDAO>())

    fun update(other: ReviewerDAO) {
        this.name = other.name
        this.birthday = other.birthday
        this.email = other.email
        this.address = other.address
        this.telephone = other.telephone
        this.city = other.city
        this.postCode = other.postCode
    }
}