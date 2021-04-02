package pt.unl.fct.grantapp.DataItemTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.grantapp.dto.GrantCallDTO
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.EvaluationPanelRepository
import pt.unl.fct.grantapp.model.GrantCallRepository
import pt.unl.fct.grantapp.model.SponsorEntityRepository
import pt.unl.fct.grantapp.model.dao.*
import pt.unl.fct.grantapp.service.DataItemService
import pt.unl.fct.grantapp.service.GrantCallService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class DataItemTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var dataItems: DataItemService


    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val dataItem = DataItemDAO()
        val dataItem2 = DataItemDAO()
        val dataItemsDAO = ArrayList(listOf(dataItem, dataItem2))
        val dataItemURL = "/grantcall"
    }

    @Test
    @WithMockUser(username = "STUDENT1", password = "STUDENT1", roles = ["STUDENT"])
    fun `Test get all grants (With Role)`() {
        Mockito.`when`(dataItems.getAll()).thenReturn(dataItemsDAO)

        mvc.perform(get(dataItemURL + "/1/dataitems"))
                .andExpect(status().isNotFound())
    }

    @Test
    fun `Test get all grants (No Role)`() {
        Mockito.`when`(dataItems.getAll()).thenReturn(dataItemsDAO)

        mvc.perform(get(dataItemURL + "/1/dataitems"))
                .andExpect(status().isNotFound())
    }

}
