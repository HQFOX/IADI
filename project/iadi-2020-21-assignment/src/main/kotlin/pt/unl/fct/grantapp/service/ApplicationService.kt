package pt.unl.fct.grantapp.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.ApplicationRepository
import pt.unl.fct.grantapp.model.dao.AppDataItemDAO
import pt.unl.fct.grantapp.model.dao.ApplicationDAO

@Service
class ApplicationService(val applications: ApplicationRepository) {

    @ResponseStatus(HttpStatus.CREATED)
    fun addOne(application: ApplicationDAO) = applications.save(application)

    fun getOne(id: Long): ApplicationDAO = applications.findById(id).orElseThrow {
        NotFoundException("Application with id $id not found")
    }

    fun getAll(): Iterable<ApplicationDAO> = applications.findAll()
    fun getDataItems(appId: Long): Iterable<AppDataItemDAO> {
        val application = applications.getItemsById(appId)
                .orElseThrow { NotFoundException("There is no application with id $appId") }
        return application.dataItems
    }


    fun deleteApplication(id: Long) = getOne(id).let { applications.delete(it) }
    fun updateApplication(id: Long, app: ApplicationDAO) = getOne(id).let { it.update(app); applications.save(it) }

    fun updateApplicationStatus(newStatus : String, id:Long) =
        getOne(id).let { it.updateStatus(newStatus) ; applications.save(it) }


    //fun getApplicationEvalPanel(id: Long) = applications.getOne(id)

}