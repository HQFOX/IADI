package pt.unl.fct.grantapp.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pt.unl.fct.grantapp.model.dao.*
import java.util.*

interface GrantCallRepository : JpaRepository<GrantCallDAO, Long> {
    @Query(value = "from GrantCall where :date >= openDate AND :date <= closeDate")
    fun getAllBetweenDates(@Param("date") date: Date): Iterable<GrantCallDAO>

    @Query(value = "from GrantCall where :date >= openDate AND :date <= closeDate AND funding >= :minimum AND funding <= :maximum")
    fun getAllFiltered(date: Date, minimum: Long, maximum: Long): Iterable<GrantCallDAO>

    @Query(value = "from GrantCall where :date < openDate OR :date > closeDate")
    fun getAllClosed(@Param("date") date: Date): Iterable<GrantCallDAO>

    @Query(value = "from GrantCall where :date >= openDate AND :date < closeDate")
    fun getAllOpen(@Param("date") date: Date): Iterable<GrantCallDAO>

    @Query(value = "select g from GrantCall g inner join fetch g.dataItems where g.id = :id")
    fun findByIdWithDataItems(id: Long): Optional<GrantCallDAO>

    @Query(value = "select g from GrantCall g inner join fetch g.applications where g.id = :id")
    fun findByIdWithApplications(@Param("id") id: Long): Optional<GrantCallDAO>

    @Query(value = "select g from GrantCall g inner join fetch g.panel where g.id = :id")
    fun findByIdWithEvaluationPanel(id: Long): Optional<GrantCallDAO>

    @Query(value = "select g from GrantCall g inner join fetch g.sponsor where g.id = :grantId")
    fun findByIdWithSponsor(grantId: Long): Optional<GrantCallDAO>

    @Query(value = "select g from GrantCall g")
    fun findAllWithApplications(): MutableList<GrantCallDAO>
}

interface SponsorEntityRepository : JpaRepository<SponsorEntityDAO, Long> {
    fun getGrantsById(id: Long): Optional<SponsorEntityDAO>
}


interface DataItemRepository : JpaRepository<DataItemDAO, Long>
interface AppDataItemRepository : JpaRepository<AppDataItemDAO, Long> {
    @Query(value = "from AppDataItem where application.id = :appId")
    fun getAppItemsByApplication(appId: Long): Optional<List<AppDataItemDAO>>
}

interface UserRepository : JpaRepository<UserDAO, Long> {
    fun findUserByUsername(username: String): Optional<UserDAO>
}

interface ReviewerRepository : JpaRepository<ReviewerDAO, Long>

interface ReviewRepository : JpaRepository<ReviewDAO, Long> {
    @Query(value = "select r from ReviewDAO r inner join fetch r.application where r.application.id = :id")
    fun findByAppIdWithAll(id: Long): Iterable<ReviewDAO>
}

interface ApplicationRepository : JpaRepository<ApplicationDAO, Long> {
    fun getItemsById(id: Long): Optional<ApplicationDAO>

    @Query(value = "select a from Application a inner join fetch a.student where a.id = :id")
    fun findByIdWithStudent(id: Long): Optional<ApplicationDAO>

    @Query(value = "select a from Application a inner join fetch a.student where a.id = :id")
    fun findByIdWithStudents(id: Long): Optional<StudentDAO>
}

interface InstitutionRepository : JpaRepository<InstitutionDAO, Long> {
    fun getInstitutionDAOById(id: Long): Optional<InstitutionDAO>
    fun findInstitutionDAOByUsername(username: String): Optional<InstitutionDAO>
}

interface StudentRepository : JpaRepository<StudentDAO, Long> {
    fun getStudentDAOById(id: Long): Optional<StudentDAO>

    @Query(value = "select s from StudentDAO s inner join fetch s.applications where s.id = :id")
    fun findByIdWithApplications(id: Long): Optional<StudentDAO>
}

interface EvaluationPanelRepository : JpaRepository<EvaluationPanelDAO, Long>{

    //@Query(value = "select e from EvaluationPanelDAO e inner join fetch e.chair where e.chair.id = :id")
    fun findEvaluationPanelDAOByChair_Id(id: Long): Iterable<EvaluationPanelDAO>
}

