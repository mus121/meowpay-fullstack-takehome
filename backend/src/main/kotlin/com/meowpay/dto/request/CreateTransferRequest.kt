package com.meowpay.dto.request

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class CreateTransferRequest(
    @field:NotNull
    val senderId: UUID,

    @field:NotNull
    val recipientId: UUID,

    @field:NotNull
    @field:DecimalMin(value = "0.01", message = "Amount must be at least 0.01 treats")
    val amount: BigDecimal,
)
