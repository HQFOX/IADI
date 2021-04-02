package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.ApplicationDTO
import javax.persistence.*

@Entity(name = "Application")
class ApplicationDAO(
        @Id
        @GeneratedValue
        var id: Long,
        var introduction: String,
        var relatedWork: String,
        var workPlan: String,
        var publications: String,
        var status: String,

        @ManyToOne
        var student: StudentDAO,

        @ManyToOne
        var grant: GrantCallDAO,

        @OneToMany
        var reviews: List<ReviewDAO>,

        @OneToMany
        var dataItems: List<AppDataItemDAO>
) {
    fun update(app: ApplicationDAO) {
        this.introduction = app.introduction
        this.relatedWork = app.relatedWork
        this.workPlan = app.workPlan
        this.publications = app.publications
        this.status = app.status
    }

    constructor(app: ApplicationDTO, grant: GrantCallDAO, student: StudentDAO) :
            this(app.applicationId, app.introduction, app.relatedWork, app.workPlan, app.publications, app.status,
                    student, grant, emptyList(), emptyList())

    constructor() : this(0,"","","","","", StudentDAO(), GrantCallDAO(), emptyList(), emptyList())

    fun updateStatus ( status: String) {
        this.status = status
    }
}