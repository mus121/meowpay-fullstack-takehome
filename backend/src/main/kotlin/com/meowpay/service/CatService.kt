package com.meowpay.service

import com.meowpay.dto.response.CatResponse
import com.meowpay.exception.CatNotFoundException
import com.meowpay.mapper.CatMapper
import com.meowpay.repository.CatRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class CatService(
    private val catRepository: CatRepository,
    private val catMapper: CatMapper,
) {

    fun findAll(): List<CatResponse> =
        catRepository.findAll()
            .sortedBy { it.name }
            .map(catMapper::toResponse)

    fun findById(id: UUID): CatResponse =
        catMapper.toResponse(
            catRepository.findById(id).orElseThrow { CatNotFoundException(id) },
        )
}
