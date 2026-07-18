package com.meowpay.mapper

import com.meowpay.dto.response.CatResponse
import com.meowpay.dto.response.CatSummary
import com.meowpay.dto.response.TransferResponse
import com.meowpay.entity.Cat
import com.meowpay.entity.Transfer
import org.springframework.stereotype.Component

@Component
class CatMapper {

    fun toResponse(cat: Cat): CatResponse =
        CatResponse(
            id = cat.id,
            name = cat.name,
            balance = cat.balance,
        )

    fun toSummary(cat: Cat): CatSummary =
        CatSummary(
            id = cat.id,
            name = cat.name,
        )
}

@Component
class TransferMapper(
    private val catMapper: CatMapper,
) {

    fun toResponse(transfer: Transfer): TransferResponse =
        TransferResponse(
            id = transfer.id,
            sender = catMapper.toSummary(transfer.sender),
            recipient = catMapper.toSummary(transfer.recipient),
            amount = transfer.amount,
            createdAt = transfer.createdAt,
        )
}
