package pt.unl.fct.grantapp.dto

import com.fasterxml.jackson.annotation.JsonFormat
import pt.unl.fct.grantapp.model.dao.DataItemDAO
import pt.unl.fct.grantapp.model.dao.GrantCallDAO
import java.util.*

data class GrantCallDTO(
        val grantCallId: Long,
        val title: String,
        val description: String,
        val requirements: String,
        val funding: Long,
        @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
        val openDate: Date,
        @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
        val closeDate: Date,
        val sponsorId: Long,
        val evalPanel: Long,
        val numberApps: Int
) {
    constructor(grant: GrantCallDAO) : this(grant.id, grant.title, grant.description, grant.requirements, grant.funding, grant.openDate, grant.closeDate, grant.sponsor.id, grant.panel.id, grant.applications.filter { it.status == "Submitted" }.count())

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false

        if (this.javaClass != other.javaClass)
            return false

        val grant = other as GrantCallDTO
        return (this.grantCallId == other.grantCallId &&
                this.title == other.title &&
                this.description == other.description &&
                this.requirements == other.requirements &&
                this.funding == other.funding &&
                this.sponsorId == other.sponsorId &&
                this.evalPanel == other.evalPanel)
    }
}


data class DataItemDTO(
        val dataItemId: Long,
        val dataType: String,
        val mandatory: Boolean
) {
    constructor(dataItem: DataItemDAO) : this(dataItem.id, dataItem.dataType, dataItem.mandatory)
}
