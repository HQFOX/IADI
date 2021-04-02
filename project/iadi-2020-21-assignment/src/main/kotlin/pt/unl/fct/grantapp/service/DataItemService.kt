package pt.unl.fct.grantapp.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.AppDataItemRepository
import pt.unl.fct.grantapp.model.DataItemRepository
import pt.unl.fct.grantapp.model.dao.AppDataItemDAO
import pt.unl.fct.grantapp.model.dao.DataItemDAO

@Service
class DataItemService(val dataItems: DataItemRepository,
                      val appDataItems: AppDataItemRepository) {
    //region Grant Data Items
    fun getAll(): Iterable<DataItemDAO> = dataItems.findAll()
    fun getAllAppDataItems(): Iterable<AppDataItemDAO> = appDataItems.findAll()

    @ResponseStatus(HttpStatus.CREATED)
    fun addOne(dataItem: DataItemDAO) = dataItems.save(dataItem)

    fun getOne(id: Long): DataItemDAO = dataItems.findById(id).orElseThrow {
        NotFoundException("Dataitem with id $id not found")
    }

    //endregion
    //region Application Data Items
    fun addAppDataItem(appDataItem: AppDataItemDAO) = appDataItems.save(appDataItem)
    fun addAppDataItems(dataItems: List<AppDataItemDAO>) = dataItems.map { appDataItems.save(it) }
    fun getAllAppDataItemsByApplication(appId: Long): Iterable<AppDataItemDAO> {
        return appDataItems.getAppItemsByApplication(appId)
                .orElseThrow { NotFoundException("There is no application with id $appId") }
    }

    fun getOneAppDataItem(id: Long) = appDataItems.getOne(id)
    fun updateAppDataItem(dataItemId: Long, data: String): AppDataItemDAO = getOneAppDataItem(dataItemId).let { it.update(data); appDataItems.save(it) }
    fun deleteAppDataItem(dataItemId: Long) = getOneAppDataItem(dataItemId).let { appDataItems.delete(it) }
    //endregion
}