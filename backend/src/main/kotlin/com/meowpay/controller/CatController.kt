package com.meowpay.controller

import com.meowpay.dto.response.CatResponse
import com.meowpay.service.CatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/cats")
class CatController(
    private val catService: CatService,
) {

    @GetMapping
    fun listCats(): List<CatResponse> = catService.findAll()

    @GetMapping("/{id}")
    fun getCat(@PathVariable id: UUID): CatResponse = catService.findById(id)
}
