package com.meowpay.unit

import com.meowpay.dto.request.CreateTransferRequest
import com.meowpay.entity.Cat
import com.meowpay.exception.CatNotFoundException
import com.meowpay.exception.InsufficientBalanceException
import com.meowpay.exception.InvalidTransferException
import com.meowpay.mapper.TransferMapper
import com.meowpay.repository.CatRepository
import com.meowpay.repository.TransferRepository
import com.meowpay.service.TransferService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class TransferServiceTest {

    @Mock
    private lateinit var catRepository: CatRepository

    @Mock
    private lateinit var transferRepository: TransferRepository

    @Mock
    private lateinit var transferMapper: TransferMapper

    @InjectMocks
    private lateinit var transferService: TransferService

    private val senderId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val recipientId = UUID.fromString("22222222-2222-2222-2222-222222222222")

    @Test
    fun `throws when sender is not found`() {
        whenever(catRepository.findByIdForUpdate(senderId)).thenReturn(null)

        assertThrows(CatNotFoundException::class.java) {
            transferService.createTransfer(
                CreateTransferRequest(senderId, recipientId, BigDecimal("10.00")),
            )
        }
    }

    @Test
    fun `throws when sender and recipient are the same`() {
        assertThrows(InvalidTransferException::class.java) {
            transferService.createTransfer(
                CreateTransferRequest(senderId, senderId, BigDecimal("10.00")),
            )
        }
    }

    @Test
    fun `throws when balance is insufficient`() {
        val sender = Cat(id = senderId, name = "Whiskers", balance = BigDecimal("5.00"))
        val recipient = Cat(id = recipientId, name = "Mittens", balance = BigDecimal("100.00"))

        whenever(catRepository.findByIdForUpdate(senderId)).thenReturn(sender)
        whenever(catRepository.findByIdForUpdate(recipientId)).thenReturn(recipient)

        assertThrows(InsufficientBalanceException::class.java) {
            transferService.createTransfer(
                CreateTransferRequest(senderId, recipientId, BigDecimal("10.00")),
            )
        }
    }

    @Test
    fun `debits sender and credits recipient on successful transfer`() {
        val sender = Cat(id = senderId, name = "Whiskers", balance = BigDecimal("100.00"))
        val recipient = Cat(id = recipientId, name = "Mittens", balance = BigDecimal("50.00"))

        whenever(catRepository.findByIdForUpdate(senderId)).thenReturn(sender)
        whenever(catRepository.findByIdForUpdate(recipientId)).thenReturn(recipient)
        whenever(transferRepository.save(any())).thenAnswer { it.arguments[0] }

        transferService.createTransfer(
            CreateTransferRequest(senderId, recipientId, BigDecimal("25.50")),
        )

        assertEquals(BigDecimal("74.50"), sender.balance)
        assertEquals(BigDecimal("75.50"), recipient.balance)
        verify(transferRepository).save(any())
    }
}
