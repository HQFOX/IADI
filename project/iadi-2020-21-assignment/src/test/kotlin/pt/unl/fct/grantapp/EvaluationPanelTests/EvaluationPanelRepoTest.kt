package pt.unl.fct.grantapp.EvaluationPanelTests

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.grantapp.model.EvaluationPanelRepository
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class EvaluationPanelRepoTest {

    @Autowired
    lateinit var panelsRepo:EvaluationPanelRepository

    companion object Constants {

    }

    @Test
    fun `basic test on findAll`() {
       // pets.deleteAll()
       // assertThat(pets.findAll().toList(), equalTo(emptyList()))
    }

    @Test
    @Transactional
    // The default equals method needs to also compare the appointments,
    // so it needs to load a lazily referred collection.
    fun `basic test on save and delete`() {

    }


}