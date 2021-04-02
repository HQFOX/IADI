package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.InstitutionDTO
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class InstitutionDAO(
        @Id
        @GeneratedValue
        override val id: Long,
        override var username: String,
        override var password: String,
        override var role: String,
        var name: String,
        var address: String,
        var contact: String,

        @OneToMany
        var students: List<StudentDAO>,

        @OneToMany
        var reviewers: List<ReviewerDAO>
) : UserDAO(id, username, password, role) {
    constructor(institution: InstitutionDTO) : this(institution.id, institution.username, institution.password, "ROLE_INSTITUTION",
            institution.name, institution.address, institution.contact, emptyList(), emptyList())

    constructor() : this(0, "", "", "", "", "", "", emptyList(), emptyList())

    fun update(institution: InstitutionDAO) {
        this.name = institution.name
        this.address = institution.address
        this.contact = institution.contact
    }
}