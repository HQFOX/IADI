package pt.unl.fct.grantapp.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.InstitutionRepository
import pt.unl.fct.grantapp.model.dao.InstitutionDAO
import pt.unl.fct.grantapp.model.dao.ReviewerDAO
import pt.unl.fct.grantapp.model.dao.StudentDAO

@Service
class InstitutionService(val institutions: InstitutionRepository) {
    fun getAll(): Iterable<InstitutionDAO> = institutions.findAll()
    fun getOne(id: Long): InstitutionDAO = institutions.findById(id).orElseThrow {
        NotFoundException("Institution with id $id not found!")
    }

    @ResponseStatus(HttpStatus.CREATED)
    fun addOne(institution: InstitutionDAO) = institutions.save(institution)
    fun updateInstitution(id: Long, institution: InstitutionDAO) = getOne(id).let { it.update(institution); institutions.save(it) }
    fun delete(id: Long) = getOne(id).let { institutions.delete(it) }


    fun getStudents(id: Long): Iterable<StudentDAO> {
        val inst = institutions.getInstitutionDAOById(id)
                .orElseThrow { NotFoundException("There is no institution entity with id $id") }
        return inst.students
    }

    fun getReviewers(id: Long): Iterable<ReviewerDAO> {
        /* val inst = institutions.getInstitutionDAOById(id)
                 .orElseThrow { NotFoundException("There is no institution entity with id $id") }
         return inst.reviewers*/
        return getOne(id).reviewers
    }
}