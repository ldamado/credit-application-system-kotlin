package me.dio.creditapplicationsystem.service

import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import java.util.UUID

interface ICreditService {
    fun save(Credit: Credit): Credit
    fun findAllByCustomer(customer: Long): List<Credit>
    fun findByCreditCode(customerId: Long, creditCode: UUID): Credit
}