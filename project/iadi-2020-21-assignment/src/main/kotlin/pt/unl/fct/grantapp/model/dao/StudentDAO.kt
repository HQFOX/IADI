package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.StudentDTO
import javax.persistence.*

@Entity
data class StudentDAO(
        @Id
        @GeneratedValue
        override val id: Long,
        override var username: String,
        override var password: String,
        override var role: String,
        var name: String,
        var birthday: String,
        var email: String,
        var address: String,
        var telephone: String,
        var city: String,
        var postcode: String,
        @ManyToOne
        val institution: InstitutionDAO,
        @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL])
        var applications: List<ApplicationDAO>
) : UserDAO(id, username, password, role) {
    constructor(student: StudentDTO, institution: InstitutionDAO) : this(student.id, student.username, student.password, "STUDENT",
            student.name, student.birthday, student.email, student.address, student.telephone, student.city, student.postcode,
            institution, emptyList())

    constructor() : this(0, "", "", "", "", "", "", "", "", "", "", InstitutionDAO(), emptyList())

    fun update(student: StudentDAO) {
        this.name = student.name;
        this.birthday = student.birthday;
        this.email = student.email;
        this.address = student.address;
        this.telephone = student.telephone;
        this.city = student.city;
        this.postcode = student.postcode;
    }
}