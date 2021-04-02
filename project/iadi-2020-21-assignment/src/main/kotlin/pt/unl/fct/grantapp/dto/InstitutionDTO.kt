package pt.unl.fct.grantapp.dto


import pt.unl.fct.grantapp.model.dao.InstitutionDAO


data class InstitutionDTO(
        override val id: Long,
        override val username: String,
        override val password: String,
        val name: String,
        val address: String,
        val contact: String
) : UserDTO(id, username, password) {
    constructor(institution: InstitutionDAO) : this(institution.id, institution.username, institution.password, institution.name, institution.address, institution.contact)
}





