package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.GrantCallDTO
import java.util.*
import javax.persistence.*

@Entity(name = "GrantCall")
data class GrantCallDAO(
        @Id
        @GeneratedValue
        var id: Long,
        var title: String,
        var description: String,
        var requirements: String,
        var funding: Long,
        var openDate: Date,
        var closeDate: Date,

        @ManyToOne
        var sponsor: SponsorEntityDAO,

        @OneToMany(mappedBy = "grant", cascade = [CascadeType.ALL])
        var dataItems: List<DataItemDAO>,

        @ManyToOne
        var panel: EvaluationPanelDAO,

        @OneToMany(mappedBy = "grant", cascade = [CascadeType.ALL])
        var applications: List<ApplicationDAO>
) {
    fun update(grant: GrantCallDAO) {
        this.title = grant.title
        this.description = grant.description
        this.requirements = grant.requirements
        this.funding = grant.funding
        this.openDate = grant.openDate
        this.closeDate = grant.closeDate
    }

    constructor(grant: GrantCallDTO, sponsor: SponsorEntityDAO, dataItems: List<DataItemDAO>, panel: EvaluationPanelDAO) : this(grant.grantCallId, grant.title, grant.description, grant.requirements, grant.funding,
            grant.openDate, grant.closeDate, sponsor, dataItems, panel, emptyList())

    constructor() : this(0, "", "", "", 1, Date(), Date(), SponsorEntityDAO(), emptyList(), EvaluationPanelDAO(), emptyList())

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false

        if (this.javaClass != other.javaClass)
            return false

        val grant = other as GrantCallDAO
        return (this.id == grant.id && this.title == grant.title && this.description == grant.description && this.requirements == grant.requirements
                && this.funding == grant.funding
                //&& this.openDate == grant.openDate && this.closeDate == grant.closeDate)
                && this.sponsor == grant.sponsor && this.dataItems == grant.dataItems && this.panel == grant.panel)
    }
}

