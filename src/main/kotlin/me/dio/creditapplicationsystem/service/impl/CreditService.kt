package me.dio.creditapplicationsystem.service.impl

import me.dio.creditapplicationsystem.repository.CreditRepository
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.exception.BusinessException
import me.dio.creditapplicationsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
) : ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        this.creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit =
            (this.creditRepository.findByCreditCode(creditCode)
                ?: throw BusinessException("Creditcode $creditCode not found"))
        if (credit.customer?.id == customerId) {
            return credit
        } else {
            throw IllegalArgumentException("Contact admin")
        }

    }

    private fun validDayFirstInstallment(dayFirstInstallment: LocalDate): Boolean {
        if (dayFirstInstallment.isBefore((LocalDate.now().plusMonths(3)))) {
            return true
        } else {
            throw BusinessException("Invalid Date selected")
        }
    }
}