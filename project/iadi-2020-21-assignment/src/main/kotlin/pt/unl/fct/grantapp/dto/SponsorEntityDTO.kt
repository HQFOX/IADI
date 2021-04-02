package pt.unl.fct.grantapp.dto

import pt.unl.fct.grantapp.model.dao.SponsorEntityDAO

data class SponsorEntityDTO(
        override val id: Long,
        override val username: String,
        override val password: String,
        val name: String,
        val address: String,
        val contact: String
) : UserDTO(id, username, password) {
    constructor(sponsor: SponsorEntityDAO) : this(sponsor.id, sponsor.username, sponsor.password, sponsor.name, sponsor.address, sponsor.contact)
}