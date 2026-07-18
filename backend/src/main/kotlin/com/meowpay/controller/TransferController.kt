package com.meowpay.controller

import com.meowpay.dto.request.CreateTransferRequest
import com.meowpay.dto.response.TransferResponse
import com.meowpay.service.TransferService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/transfers")
class TransferController(
    private val transferService: TransferService,
) {

    @GetMapping
    fun listTransfers(): List<TransferResponse> = transferService.findRecent()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransfer(@Valid @RequestBody request: CreateTransferRequest): TransferResponse =
        transferService.createTransfer(request)
}
