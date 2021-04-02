package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.SponsorEntityDTO
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
data class SponsorEntityDAO(
        override val id: Long,
        override var username: String,
        override var password: String,
        override var role: String,
        var name: String,
        var address: String,
        var contact: String,
        @OneToMany(mappedBy = "sponsor", cascade = [CascadeType.ALL])
        var grantCalls: List<GrantCallDAO>
) : UserDAO(id, username, password, role) {
    fun update(sponsor: SponsorEntityDAO) {
        this.name = sponsor.name
        this.address = sponsor.address
        this.contact = sponsor.contact
    }

    constructor(sponsor: SponsorEntityDTO) : this(sponsor.id, sponsor.username, sponsor.password, "ROLE_SPONSOR", sponsor.name, sponsor.address, sponsor.contact,
            emptyList<GrantCallDAO>())

    constructor() : this(0, "", "", "", "", "", "", emptyList())
}