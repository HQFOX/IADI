package pt.unl.fct.grantapp.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.StudentRepository
import pt.unl.fct.grantapp.model.dao.ApplicationDAO
import pt.unl.fct.grantapp.model.dao.StudentDAO

@Service
class StudentService(val students: StudentRepository) {
    fun getAll(): Iterable<StudentDAO> = students.findAll()
    fun getOne(id: Long): StudentDAO = students.findById(id).orElseThrow {
        NotFoundException("Student with id $id not found!")
    }

    fun getApplications(id: Long): Iterable<ApplicationDAO> {
        val std = students.findByIdWithApplications(id).orElseThrow {
            NotFoundException("Cannot find student with id $id")
        }
        return std.applications
    }

    @ResponseStatus(HttpStatus.CREATED)
    fun addOne(student: StudentDAO) = students.save(student)

    fun deleteStudent(id: Long) = getOne(id).let { students.delete(it) }
    fun updateStudent(id: Long, student: StudentDAO) = getOne(id).let { it.update(student); students.save(it) }
}