package pt.unl.fct.grantapp.config

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.*
import java.text.SimpleDateFormat
import java.util.*

@Component
class DbSeeder(
        val sponsors: SponsorEntityService,
        val grantCalls: GrantCallService,
        val dataItems: DataItemService,
        val students: StudentService,
        val institutions: InstitutionService,
        val reviewers: ReviewerService,
        val evaluationPanels: EvaluationPanelService,
        val applications: ApplicationService,
        val reviews: ReviewService
) : CommandLineRunner {
    val runSeeder = false

    override fun run(vararg args: String?) {
        if (!runSeeder) {
            print("Database seeder disabled.")
            return
        }

        val sponsor1 = sponsors.addOne(SponsorEntityDAO(1, "FCT", BCryptPasswordEncoder().encode("FCT"), "SPONSOR", "FCT", "Costa", "@FCT", emptyList()))
        val sponsor2 = sponsors.addOne(SponsorEntityDAO(2, "IPS", BCryptPasswordEncoder().encode("IPS"), "SPONSOR", "IPS", "Setubal", "@IPS", emptyList()))

        val institution1 = institutions.addOne(InstitutionDAO(1, "Inst1", BCryptPasswordEncoder().encode("Inst1"), "INSTITUTION", "Institution 1", "IDK", "IDK", emptyList(), emptyList()))
        val institution2 = institutions.addOne(InstitutionDAO(1, "Inst2", BCryptPasswordEncoder().encode("Inst2"), "INSTITUTION", "Institution 2", "IDK", "IDK", emptyList(), emptyList()))

        val student1 = students.addOne(StudentDAO(1, "stud1", BCryptPasswordEncoder().encode("stud1"), "STUDENT",
                "Student1", "birthday", "email", "address", "telephone", "city", "postcode",
                institution1, emptyList()))
        val student2 = students.addOne(StudentDAO(2, "stud2", BCryptPasswordEncoder().encode("stud2"), "STUDENT",
                "Student2", "birthday", "email", "address", "telephone", "city", "postcode",
                institution1, emptyList()))
        students.addOne(StudentDAO(3, "stud3", BCryptPasswordEncoder().encode("stud3"), "STUDENT",
                "Student3", "birthday", "email", "address", "telephone", "city", "postcode",
                institution2, emptyList()))


        val reviewer1 = reviewers.addOne(ReviewerDAO(1, "rev1", BCryptPasswordEncoder().encode("rev1"), "REVIEWER", "Rev1", "03/11/1994", "rev1@revs.com", "address", "phone", "city", "postCode", institution1, emptyList(), emptyList()))
        val reviewer2 = reviewers.addOne(ReviewerDAO(1, "rev2", BCryptPasswordEncoder().encode("rev2"), "REVIEWER", "Rev2", "11/03/1994", "rev2@revs.com", "address", "phone", "city", "postCode", institution2, emptyList(), emptyList()))


        val evalPanel1 = evaluationPanels.addOne(EvaluationPanelDAO(1, reviewer1, emptyList(), emptyList(), emptyList()))
        val evalPanel2 = evaluationPanels.addOne(EvaluationPanelDAO(2, reviewer2, emptyList(), emptyList(), emptyList()))
        //evaluationPanels.addOneMember(evalPanel1.id, reviewer1)
        //evaluationPanels.addOneMember(evalPanel1.id, reviewer2)

        val date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")
        val previousDate = SimpleDateFormat("dd/MM/yyyy").parse("20/12/2020")
        val previousDateMinus = SimpleDateFormat("dd/MM/yyyy").parse("10/10/2019")
        val futureDate = SimpleDateFormat("dd/MM/yyyy").parse("01/02/2021")
        val grantCall1 = grantCalls.addOne(GrantCallDAO(1, "Bolsa FCT", "Bolsa maravilhosa para os alunos", "Média >= 14", 999, previousDate, futureDate, sponsor1, emptyList(), evalPanel1, emptyList()))
        val grantCall2 = grantCalls.addOne(GrantCallDAO(2, "Bolsitas Novas", "nem sei", "Média > 10", 1, previousDateMinus, date, sponsor2, emptyList(), evalPanel2, emptyList()))

        val dataItem1 = dataItems.addOne(DataItemDAO(1, "Curriculum Vitae", true, grantCall1, emptyList()))
        val dataItem2 = dataItems.addOne(DataItemDAO(2, "Carta de Recomendação", false, grantCall1, emptyList()))
        dataItems.addOne(DataItemDAO(3, "Curriculum Vitae", false, grantCall2, emptyList()))

        val application1 = applications.addOne(ApplicationDAO(1, "Very good intro", "Field work", "Sleep", "none", "Submitted", student1, grantCall1, emptyList(), emptyList()))
        val application2 = applications.addOne(ApplicationDAO(2, "Very good intro2", "Field work2", "Sleep2", "none2", "Submitted", student2, grantCall1, emptyList(), emptyList()))
        applications.addOne(ApplicationDAO(3, "Very good intro3", "Field work3", "Sleep3", "none3", "Submitted", student2, grantCall2, emptyList(), emptyList()))

        dataItems.addAppDataItem(AppDataItemDAO(1, "MyCurriculum.pdf", application1, dataItem1))
        dataItems.addAppDataItem(AppDataItemDAO(2, "Cur2020.pdf", application1, dataItem2))
        dataItems.addAppDataItem(AppDataItemDAO(2, "RecomendationLetter.pdf", application2, dataItem1))

        reviews.addOne(ReviewDAO(1, "Esta aplicação é muito boa....", application1, reviewer1, evalPanel1))
        reviews.addOne(ReviewDAO(2, "Não acho assim tão boa...", application1, reviewer2, evalPanel1))

        print("\n\n\t( ͡o ͜ʖ ͡o) Database Seed Completed (╯ ͠° ͟ʖ ͡°)╯┻━┻\n")
    }

}