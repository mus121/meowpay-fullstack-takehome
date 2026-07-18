package com.meowpay.repository

import com.meowpay.entity.Cat
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CatRepository : JpaRepository<Cat, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cat c WHERE c.id = :id")
    fun findByIdForUpdate(@Param("id") id: UUID): Cat?
}
