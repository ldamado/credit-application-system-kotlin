package me.dio.creditapplicationsystem.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.creditapplicationsystem.entity.Address
import me.dio.creditapplicationsystem.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Invalid input. Try again") val firstName: String,
    @field:NotEmpty(message = "Invalid input. Try again") val lastName: String,
    @field:NotEmpty(message = "Invalid input. Try again")
    @field:CPF(message = "Invalid CPF input. Try again") val cpf: String,
    @field:NotNull(message = "Invalid income input. Try again") val income: BigDecimal,
    @field:Email(message = "Invalid email format. Try again")
    @field:NotEmpty(message = "Invalid email input. Try again") val email: String,
    @field:NotEmpty(message = "Invalid input. Try again") val password: String,
    @field:NotEmpty(message = "Invalid zip code input. Try again") val zipCode: String,
    @field:NotEmpty(message = "Invalid street input. Try again") val street: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )

    )

}
