package com.meowpay.service

import com.meowpay.dto.request.CreateTransferRequest
import com.meowpay.dto.response.TransferResponse
import com.meowpay.entity.Transfer
import com.meowpay.exception.CatNotFoundException
import com.meowpay.exception.InsufficientBalanceException
import com.meowpay.exception.InvalidTransferException
import com.meowpay.mapper.TransferMapper
import com.meowpay.repository.CatRepository
import com.meowpay.repository.TransferRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
class TransferService(
    private val catRepository: CatRepository,
    private val transferRepository: TransferRepository,
    private val transferMapper: TransferMapper,
) {

    @Transactional(readOnly = true)
    fun findRecent(): List<TransferResponse> =
        transferRepository.findRecentTransfers()
            .map(transferMapper::toResponse)

    @Transactional
    fun createTransfer(request: CreateTransferRequest): TransferResponse {
        validateRequest(request)

        val sender = catRepository.findByIdForUpdate(request.senderId)
            ?: throw CatNotFoundException(request.senderId)
        val recipient = catRepository.findByIdForUpdate(request.recipientId)
            ?: throw CatNotFoundException(request.recipientId)

        if (sender.balance < request.amount) {
            throw InsufficientBalanceException(sender.name, request.amount, sender.balance)
        }

        sender.balance = sender.balance.subtract(request.amount)
        recipient.balance = recipient.balance.add(request.amount)

        val transfer = Transfer(
            sender = sender,
            recipient = recipient,
            amount = request.amount,
        )

        return transferMapper.toResponse(transferRepository.save(transfer))
    }

    private fun validateRequest(request: CreateTransferRequest) {
        if (request.senderId == request.recipientId) {
            throw InvalidTransferException("A cat cannot send treats to themselves")
        }

        if (request.amount <= BigDecimal.ZERO) {
            throw InvalidTransferException("Transfer amount must be greater than zero")
        }

        if (request.amount.scale() > 2) {
            throw InvalidTransferException("Amount supports at most 2 decimal places")
        }
    }
}
