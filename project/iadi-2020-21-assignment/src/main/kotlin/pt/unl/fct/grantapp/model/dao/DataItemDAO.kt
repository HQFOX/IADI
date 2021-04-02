package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.DataItemDTO
import javax.persistence.*

@Entity(name= "DataItem")
data class DataItemDAO(
        @Id
        @GeneratedValue
        var id: Long,
        var dataType: String,
        var mandatory: Boolean,

        @ManyToOne
        var grant: GrantCallDAO,

        @OneToMany
        var appDataItems: List<AppDataItemDAO>
) {
    constructor(dataItem: DataItemDTO, grant: GrantCallDAO) : this(dataItem.dataItemId, dataItem.dataType, dataItem.mandatory, grant, emptyList())
    constructor() : this(0, "", false, GrantCallDAO(), emptyList())

    fun update(dataItem: DataItemDAO) {
        this.dataType = dataItem.dataType
        this.mandatory = dataItem.mandatory
    }
}