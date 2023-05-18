package me.dio.creditapplicationsystem.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid input. Try again") val creditValue: BigDecimal,
    @field: Min(value = 1) @field: Max(value = 48) val numberOfInstallments: Int,
    @field: Future (message = "It must be a future date. Try again") val dayFirstInstallment: LocalDate,
    @field:NotNull(message = "Invalid input. Try again") val customerId: Long
) {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        numberOfInstallments = this.numberOfInstallments,
        dayFirstInstallment = this.dayFirstInstallment,
        customer = Customer(id = this.customerId)
    )

}