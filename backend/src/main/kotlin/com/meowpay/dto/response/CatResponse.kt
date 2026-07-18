package com.meowpay.dto.response

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class CatResponse(
    val id: UUID,
    val name: String,
    val balance: BigDecimal,
)

data class TransferResponse(
    val id: UUID,
    val sender: CatSummary,
    val recipient: CatSummary,
    val amount: BigDecimal,
    val createdAt: Instant,
)

data class CatSummary(
    val id: UUID,
    val name: String,
)

data class ErrorResponse(
    val message: String,
    val timestamp: Instant = Instant.now(),
)
