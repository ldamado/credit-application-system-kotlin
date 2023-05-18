package me.dio.creditapplicationsystem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.creditapplicationsystem.dto.request.CustomerDto
import me.dio.creditapplicationsystem.dto.request.CustomerUpdateDto
import me.dio.creditapplicationsystem.entity.Address
import me.dio.creditapplicationsystem.entity.Customer
import me.dio.creditapplicationsystem.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.net.URL
import javax.print.attribute.standard.Media
import kotlin.random.Random


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration

class CustomerResourceTest {

    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
    }

    @BeforeEach fun setup() = customerRepository.deleteAll()
    @AfterEach fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return 201 status`() {
        //given
        val customerDto: CustomerDto = buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("João"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joao@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345678"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua 1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(10000.0))
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should not save a customer with same CPF and return 409 status` () {
        //given
        customerRepository.save(buildCustomerDto().toEntity())
        val customerDto: CustomerDto = buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict detected! Refer to documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with firstName empty and return 400 status` () {
        //given
        val customerDto: CustomerDto = buildCustomerDto(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Refer to documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id and return 200 status` () {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("João"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joao@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345678"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua 1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(10000.0))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer when invalid id and retur 400 status` () {
        //given
        val invalidId: Long = Random.nextLong()
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$invalidId")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Refer to documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete customer by id and return 204 status` () {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete customer by id and return 400 status` () {
        //given
        val invalidId: Long = Random.nextLong()
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${invalidId}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Refer to documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should update customer and return status 200` () {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDtoDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDtoDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("JoãoUpdate"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("SilvaUpdate"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joao@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("45678901"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua Update"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(50000.0))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update customer with invalid id and return status 400` () {
        //given
        val invalidId: Long = Random.nextLong()
        val customerUpdateDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${invalidId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Refer to documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDto (
        firstName: String = "João",
        lastName: String = "Silva",
        cpf: String = "28475934625",
        email: String = "joao@email.com",
        password: String = "12345",
        zipCode: String = "12345678",
        street: String = "Rua 1",
        income: BigDecimal = BigDecimal.valueOf(10000.0),
    ) = CustomerDto (
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        income = income,
        zipCode = zipCode,
        street = street
    )

    private fun buildCustomerUpdateDto (
        firstName: String = "JoãoUpdate",
        lastName: String = "SilvaUpdate",
        income: BigDecimal = BigDecimal.valueOf(50000.0),
        zipCode: String = "45678901",
        street: String = "Rua Update"
    ): CustomerUpdateDto = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}