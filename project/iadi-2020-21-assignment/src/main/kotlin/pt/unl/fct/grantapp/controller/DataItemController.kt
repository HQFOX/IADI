package pt.unl.fct.grantapp.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.grantapp.dto.AppDataItemDTO
import pt.unl.fct.grantapp.dto.DataItemDTO
import pt.unl.fct.grantapp.model.dao.AppDataItemDAO
import pt.unl.fct.grantapp.model.dao.DataItemDAO
import pt.unl.fct.grantapp.service.ApplicationService
import pt.unl.fct.grantapp.service.DataItemService
import pt.unl.fct.grantapp.service.GrantCallService

@RestController
@RequestMapping("/dataitem")
class DataItemController(
        val grantCallService: GrantCallService,
        val applications: ApplicationService,
        val dataItemService: DataItemService) {

    //region Admin GETS
    @ApiOperation(value = "Get all grant dataItems")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved all dataitems."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/grants")
    fun getAllDI() = dataItemService.getAll().map { DataItemDTO(it) }

    @ApiOperation(value = "Get all application dataItems")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved all dataitems."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden.")
    ])
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/applications")
    fun getAllDAtaItems(): Iterable<AppDataItemDTO> = dataItemService.getAllAppDataItems().map { AppDataItemDTO(it) }
    //endregion
    //region Grants data items
    @ApiOperation(value = "Add a new data item to grant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully created new data item."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The grant doesn't exists.")
    ])
    @PreAuthorize("hasRole('SPONSOR') and @securityService.canEditGrant(principal, #id)")
    @PostMapping("/grants/{id}/dataitems")
    fun addDataItem(@PathVariable id: Long,
                    @RequestBody dataItem: DataItemDTO): DataItemDTO =
            DataItemDTO(grantCallService.addOneDataItem(DataItemDAO(dataItem, grantCallService.getOne(id))))

    @ApiOperation(value = "Delete data item to grant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted the data item."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The data item doesn't exists.")
    ])
    @PreAuthorize("hasRole('SPONSOR') and @securityService.canEditGrant(principal, #id)")
    @DeleteMapping("/grants/{id}/dataitems/{dataItemId}")
    fun delDataItem(@PathVariable id: Long,
                    @PathVariable dataItemId: Long) = grantCallService.deleteDataItem(dataItemId)

    @ApiOperation(value = "Update the data item of grant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated data item."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The data item doesn't exists.")
    ])
    @PreAuthorize("hasRole('SPONSOR') and @securityService.canEditGrant(principal, #id)")
    @PutMapping("/grants/{id}/dataitems/{dataItemId}")
    fun updateDataItem(@PathVariable id: Long, @PathVariable dataItemId: Long, @RequestBody dataItem: DataItemDTO) = DataItemDTO(grantCallService.updateDataItem(dataItemId, DataItemDAO(dataItem, grantCallService.getOne(id))))
    //endregion
    //region Applications data items
    @ApiOperation(value = "Get application data items")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully obtained application data items."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The application doesn't exist.")
    ])
    @PreAuthorize("hasRole('STUDENT') and @securityService.canModifyApplication(principal, #id)")
    @GetMapping("/applications/{id}")
    fun getApplicationDataItems(@PathVariable id: Long): Iterable<AppDataItemDTO> = applications.getDataItems(id).map { AppDataItemDTO(it) }

    @ApiOperation(value = "Create application data item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully created application data item."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The application doesn't exist.")
    ])
    @PreAuthorize("hasRole('STUDENT') and @securityService.canModifyApplication(principal, #id)")
    @PostMapping("/applications/{id}")
    fun addApplicationDataItems(@PathVariable id: Long,
                                @RequestBody dataItem: AppDataItemDTO): AppDataItemDTO =
            AppDataItemDTO(dataItemService.addAppDataItem(AppDataItemDAO(dataItem, applications.getOne(id), dataItemService.getOne(dataItem.dataItemId))))

    @ApiOperation(value = "Update application data item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated application data item."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The application data item doesn't exist.")
    ])
    @PreAuthorize("hasRole('STUDENT') and @securityService.canModifyApplication(principal, #id)")
    @PutMapping("/applications/{id}/dataitem/{dataItemId}")
    fun putApplicationDataItems(@PathVariable id: Long, @PathVariable dataItemId: Long, @RequestBody dataItem: AppDataItemDTO) = AppDataItemDTO(dataItemService.updateAppDataItem(dataItemId, dataItem.data))

    @ApiOperation(value = "Delete application data item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted application data item."),
        ApiResponse(code = 401, message = "You are not authorized to reach this resource."),
        ApiResponse(code = 403, message = "The resource you are trying to reach is forbidden."),
        ApiResponse(code = 404, message = "The application data item doesn't exist.")
    ])
    @PreAuthorize("hasRole('STUDENT') and @securityService.canModifyApplication(principal, #id)")
    @DeleteMapping("/applications/{id}/dataitem/{dataItemId}")
    fun deleteApplicationDataItems(@PathVariable id: Long, @PathVariable dataItemId: Long) = dataItemService.deleteAppDataItem(dataItemId)
    //endregion
}