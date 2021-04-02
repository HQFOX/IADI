package pt.unl.fct.grantapp.dto

import pt.unl.fct.grantapp.model.dao.StudentDAO

data class StudentDTO(
        override val id: Long,
        override val username: String,
        override val password: String,
        val name: String,
        val birthday: String,
        val email: String,
        val address: String,
        val telephone: String,
        val city: String,
        val postcode: String,
        val institutionID: Long
) : UserDTO(id, username, password) {
    constructor(student: StudentDAO) : this(student.id,
            student.username, student.password, student.name, student.birthday,
            student.email, student.address,
            student.telephone, student.city, student.postcode,
            student.institution.id
    )
}