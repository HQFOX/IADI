package pt.unl.fct.grantapp.SponsorTests

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.grantapp.SponsorTests.SponsorEntityControllerTest.Companion.sponsor1
import pt.unl.fct.grantapp.model.SponsorEntityRepository
import pt.unl.fct.grantapp.model.dao.SponsorEntityDAO
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class SponsorEntityRepositoryTest {

    @Autowired
    lateinit var sponsors: SponsorEntityRepository

    companion object {
        val sponsorOne = SponsorEntityDAO(1,"se1","se1","ROLE_SPONSOR","name1","address1","contact1", emptyList())
        val sponsorTwo = SponsorEntityDAO(2,"se2","se2","ROLE_SPONSOR","name2","address2","contact2", emptyList())
    }

    @Test
    fun `findAll empty after delete`() {
        sponsors.deleteAll()
        assertThat(sponsors.findAll().toList(), equalTo(emptyList()))
    }

    @Transactional
    @Test
    fun `test save`()
    {
        sponsors.deleteAll()
        assertThat(sponsors.findAll().toList(), equalTo(emptyList()))

        //val newSponsor = sponsors.save(sponsorOne)
        //assertThat(sponsors.findOne(1), equalTo(newSponsor))
    }

    @Transactional
    @Test
    fun `test findAll with grant calls associated`() {
        sponsors.deleteAll()
        assertThat(sponsors.findAll().toList(), equalTo(emptyList()))

        val newSponsor = sponsors.save(sponsorOne)
        assertThat(sponsors.getOne(newSponsor.id).name, equalTo(sponsorOne.name))
        assertThat(sponsors.findAll().toList(), equalTo(listOf(newSponsor)))

        val newSponsor2 = sponsors.save(sponsorTwo)
        assertThat(sponsors.findAll().toList(), equalTo(listOf(newSponsor,newSponsor2)))
    }

}