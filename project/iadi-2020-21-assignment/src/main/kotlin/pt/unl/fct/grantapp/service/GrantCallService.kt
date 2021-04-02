package pt.unl.fct.grantapp.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.ApplicationRepository
import pt.unl.fct.grantapp.model.DataItemRepository
import pt.unl.fct.grantapp.model.GrantCallRepository
import pt.unl.fct.grantapp.model.dao.*
import java.util.*
import kotlin.math.max


@Service
class GrantCallService(val grantCalls: GrantCallRepository,
                       val dataItems: DataItemRepository,
                       val applications: ApplicationRepository) {

    fun getAll(): MutableList<GrantCallDAO> = grantCalls.findAll()
    fun getAllWithApplications() : MutableList<GrantCallDAO> = grantCalls.findAllWithApplications()
    fun getOne(id: Long): GrantCallDAO = grantCalls.findById(id).orElseThrow {
        NotFoundException("Grant call with id $id not found.")
    }

    @ResponseStatus(HttpStatus.CREATED)
    fun addOne(grantCall: GrantCallDAO) = grantCalls.save(grantCall)

    fun delete(id: Long) = getOne(id).let { grantCalls.delete(it) }
    fun updateGrant(id: Long, grant: GrantCallDAO) = getOne(id).let { it.update(grant); grantCalls.save(it) }
    fun getAllByDate(date: Date): Iterable<GrantCallDAO> = grantCalls.getAllBetweenDates(date)
    fun getAllFiltered(date: Date, minimum: Long, maximum: Long): Iterable<GrantCallDAO> =
            grantCalls.getAllFiltered(date,minimum, maximum)
    fun openGrants(): Iterable<GrantCallDAO> = getAllByDate(Date())
    fun closedGrants(): Iterable<GrantCallDAO> = grantCalls.getAllClosed(Date())
    fun openingSoon(): Iterable<GrantCallDAO> {
        var dt = Date()
        val c = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.DATE, 7)
        dt = c.time
        return getAllByDate(dt)
    }

    //region App data items
    fun getAllDataItems(id: Long): Iterable<DataItemDAO> {
        val grant = grantCalls.findByIdWithDataItems(id).orElseThrow {
            NotFoundException("Cannot find grant with id $id")
        }
        return grant.dataItems
    }

    @ResponseStatus(HttpStatus.CREATED)
    fun addOneDataItem(dataItem: DataItemDAO) = dataItems.save(dataItem)
    fun deleteDataItem(id: Long) = dataItems.getOne(id).let { dataItems.delete(it) }
    fun updateDataItem(id: Long, dataItem: DataItemDAO) = dataItems.getOne(id).let { it.update(dataItem); dataItems.save(it) }
    //endregion

    fun getAllApplications(id: Long): Iterable<ApplicationDAO> {
        val grant = grantCalls.findByIdWithApplications(id).orElseThrow {
            NotFoundException("Cannot find grant with id $id")
        }
        return grant.applications
    }

    fun getAllStudents(id: Long): Iterable<StudentDAO> {
        val grant = grantCalls.findByIdWithApplications(id).orElseThrow {
            NotFoundException("Cannot find grant with id $id")
        }

        val students = grant.applications.map {
            applications.findByIdWithStudents(it.id).orElseThrow {
                NotFoundException("Application student with id $id not found.")
            }
        }
        return students
    }

    fun getAllEvaluationPanel(id: Long): EvaluationPanelDAO {
        val grant = grantCalls.findByIdWithEvaluationPanel(id).orElseThrow {
            NotFoundException("Cannot find grant with id $id")
        }
        return grant.panel
    }


}