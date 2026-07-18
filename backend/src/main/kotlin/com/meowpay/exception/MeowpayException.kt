package com.meowpay.exception

import java.util.UUID

open class MeowpayException(
    message: String,
) : RuntimeException(message)

class CatNotFoundException(
    catId: UUID,
) : MeowpayException("Cat not found: $catId")

class InsufficientBalanceException(
    catName: String,
    requested: java.math.BigDecimal,
    available: java.math.BigDecimal,
) : MeowpayException(
    "$catName only has $available treats but you tried to send $requested",
)

class InvalidTransferException(
    message: String,
) : MeowpayException(message)
