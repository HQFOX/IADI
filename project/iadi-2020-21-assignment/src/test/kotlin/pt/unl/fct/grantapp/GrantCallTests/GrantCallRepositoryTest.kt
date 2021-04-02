package pt.unl.fct.grantapp.GrantCallTests


import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.grantapp.model.*
import pt.unl.fct.grantapp.model.dao.*
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class GrantCallRepositoryTest {

    @Autowired
    lateinit var grants: GrantCallRepository

    @Autowired
    lateinit var sponsors: SponsorEntityRepository

    @Autowired
    lateinit var institutions: InstitutionRepository

    @Autowired
    lateinit var reviewers: ReviewerRepository

    @Autowired
    lateinit var evalPanels: EvaluationPanelRepository


    companion object Constants {
        val pastDate = SimpleDateFormat("dd/MM/yyyy").parse("31/12/1900")
        val futureDate = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2075")
        val currentDate = Date()
        var sponsor = SponsorEntityDAO(-1L, "FCT", BCryptPasswordEncoder().encode("FCT"), "SPONSOR", "FCT", "Costa", "@FCT", emptyList())
        var institution = InstitutionDAO(-1L, "Inst1", BCryptPasswordEncoder().encode("Inst1"), "INSTITUTION", "Institution 1", "IDK", "IDK", emptyList(), emptyList())
        var reviewer = ReviewerDAO(-1L, "Rev1", BCryptPasswordEncoder().encode("Rev1"), "REVIEWER", "Rev1", "03/11/1994", "rev1@revs.com", "address", "phone", "city", "postCode", institution, emptyList(), emptyList())
        var evalPanel = EvaluationPanelDAO(-1L, reviewer, emptyList(), emptyList(), emptyList())

        var grant1 = GrantCallDAO(-1L, "Bolsa FCT", "Bolsa maravilhosa para os alunos", "Média >= 14", 999, pastDate, futureDate, sponsor, emptyList(), evalPanel, emptyList())
        var grant2 = GrantCallDAO(-1L, "Bolsa Numero 2", "DESC", "Média >= 15", 9199, pastDate, pastDate, sponsor, emptyList(), evalPanel, emptyList())
        var grant3 = GrantCallDAO(-1L, "Bolsa Numero 3", "DESC", "Média >= 15", 919329, pastDate, pastDate, sponsor, emptyList(), evalPanel, emptyList())
    }


    @Test
    fun `Test findAll`() {
        grants.deleteAll()
        assertEquals(grants.findAll().toList().size, 0)
    }

    @Test
    @Transactional
    fun `Test SaveAll and FindAll`() {
        SaveData()
        assertEquals(grants.findAll().toList().size, 3)
    }

    @Test
    @Transactional
    fun `Test FindOne ByID`() {
        SaveData()
        val grantFind = grants.findById(grant1.id)
        assertNotNull(grantFind)
        assertEquals(grantFind.get().id, grant1.id)
        assertEquals(grantFind.get().title, grant1.title)
        assertEquals(grantFind.get().funding, grant1.funding)
        assertEquals(grantFind.get().description, grant1.description)
    }

    @Test
    @Transactional
    fun `Test getAllBetweenDates`() {
        SaveData()
        val _grant = grants.getAllBetweenDates(currentDate)

        assertEquals(_grant, listOf(grant1))
    }

    @Test
    @Transactional
    fun `Test getAllOpen`() {
        SaveData()
        assertEquals(grants.getAllOpen(Date()).toList().size, 1)
    }

    @Test
    @Transactional
    fun `Test getAllClosed`() {
        SaveData()
        assertEquals(grants.getAllClosed(Date()), listOf(grant2, grant3))
    }

    @Test
    @Transactional
    fun `Test findById with Evaluation Panel`() {
        SaveData()
        val findEvalPanel = grants.findByIdWithEvaluationPanel(grant1.id)
        assertEquals(findEvalPanel.isPresent, true)
        assertEquals(findEvalPanel.get().panel, evalPanel)
    }

    @Test
    @Transactional
    fun `Test findById with Applications`() {
        SaveData()
        val grantApps = grants.findByIdWithApplications(grant1.id)
        // No applications submmited
        assertEquals(grantApps.isPresent, false)
    }

    @Test
    @Transactional
    fun `Test findById with Sponsor`() {
        SaveData()
        val findGrant = grants.findByIdWithSponsor(grant1.id)
        assertEquals(findGrant.isPresent, true)
        assertEquals(findGrant.get().sponsor, sponsor)
    }

    fun SaveData() {
        sponsor = sponsors.save(sponsor)
        institution = institutions.save(institution)

        reviewer.institution = institution
        reviewer = reviewers.save(reviewer)

        evalPanel.chair = reviewer
        evalPanel = evalPanels.save(evalPanel)

        grant1.panel = evalPanel
        grant2.panel = evalPanel
        grant3.panel = evalPanel
        grant1.sponsor = sponsor
        grant2.sponsor = sponsor
        grant3.sponsor = sponsor

        // Bolsa abertas
        grant1 = grants.save(grant1)

        // Bolsa fechadas
        grant2 = grants.save(grant2)
        grant3 = grants.save(grant3)
    }
}


