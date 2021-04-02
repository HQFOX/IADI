package pt.unl.fct.grantapp.GrantCallTests

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.DataItemRepository
import pt.unl.fct.grantapp.model.GrantCallRepository
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.GrantCallService
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class GrantCallServiceTest {

    @Autowired
    lateinit var grants: GrantCallService

    @MockBean
    lateinit var repo: GrantCallRepository

    @MockBean
    lateinit var dataItems: DataItemRepository

    companion object Constants {
        var date = SimpleDateFormat("dd/MM/yyyy").parse("31/12/2050")
        val grant1 = GrantCallDAO(1, "Bolsa FCT","Bolsa maravilhosa para os alunos", "Média >= 14", 999, Date(), date, SponsorEntityDAO(), emptyList(), EvaluationPanelDAO(), emptyList())
        val grant2 = GrantCallDAO(2, "Bolsa IPS","Bolsa maravilhosa para os alunos", "Média >= 14", 666, Date(), date, SponsorEntityDAO(), emptyList(), EvaluationPanelDAO(), emptyList())

        // Add to the list
        val grantsDAO = mutableListOf(grant1, grant2)
    }


    @Test
    fun `Test getAll`(){
        Mockito.`when`(repo.findAll()).thenReturn(grantsDAO)
        assertEquals(grants.getAll(), grantsDAO as List<GrantCallDAO>)
    }

    @Test
    fun `Test getOne`(){
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(grant1));

        assertThat(grants.getOne(1L), equalTo(grant1))
    }

    @Test(expected = NotFoundException::class)
    fun `Test getOne Non existent`(){
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())
        grants.getOne(999)
    }

    @Test
    fun `Test addOne`(){
        Mockito.`when`(repo.save(Mockito.any(GrantCallDAO::class.java)))
                .then {
                    val grant:GrantCallDAO = it.getArgument(0)
                    assertEquals(grant.id, grant1.id)
                    assertEquals(grant.title, grant1.title)
                    assertEquals(grant.funding, grant1.funding)
                    grant
                }

        grants.addOne(grant1)
    }

    @Test
    fun `Test delete`(){
        repo.deleteAll()
        assertEquals(repo.findAll().toList().size, 0)
    }

    @Test
    fun `Test getAllDataItems of a Grant`(){
        grant1.dataItems = emptyList()
        Mockito.`when`(repo.findByIdWithDataItems(grant1.id)).thenReturn(Optional.of(grant1))
        assertEquals(grants.getAllDataItems(grant1.id), grant1.dataItems)
    }

    @Test
    fun `Test add DataItems to grant`(){
        val dataItem = DataItemDAO(0, "String", true, grant1, emptyList())
        grant1.dataItems = emptyList()

        Mockito.`when`(dataItems.save(Mockito.any(DataItemDAO::class.java)))
                .then {
                    val item:DataItemDAO = it.getArgument(0)
                    assertEquals(item.id, 0L)
                    assertEquals(item.mandatory, dataItem.mandatory)
                    assertEquals(item.dataType, dataItem.dataType)
                    item
                }
        grants.addOneDataItem(dataItem)
    }
}