package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.AppDataItemDTO
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity(name = "AppDataItem")
class AppDataItemDAO(
        @Id
        @GeneratedValue
        var id: Long,
        var data: String,

        @ManyToOne
        var application: ApplicationDAO,

        @ManyToOne
        var dataItem: DataItemDAO
) {
    // Can only update object data.
    fun update(newData: String) {
        this.data = newData
    }

    constructor(appDataItem: AppDataItemDTO, application: ApplicationDAO, dataItem: DataItemDAO) :
            this(appDataItem.appDataItemId, appDataItem.data, application, dataItem)

    constructor() : this(0, "", ApplicationDAO(), DataItemDAO())
}