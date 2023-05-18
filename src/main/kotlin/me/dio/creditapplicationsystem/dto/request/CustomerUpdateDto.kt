package me.dio.creditapplicationsystem.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.creditapplicationsystem.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "Invalid input. Try again") val firstName: String,
    @field:NotEmpty(message = "Invalid input. Try again") val lastName: String,
    @field:NotNull(message = "Invalid income input. Try again") val income: BigDecimal,
    @field:NotEmpty(message = "Invalid input. Try again") val zipCode: String,
    @field:NotEmpty(message = "Invalid input. Try again") val street: String

) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer

    }
}
