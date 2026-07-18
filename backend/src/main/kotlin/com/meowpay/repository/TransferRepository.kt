package com.meowpay.repository

import com.meowpay.entity.Transfer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface TransferRepository : JpaRepository<Transfer, UUID> {

    @Query(
        """
        SELECT t FROM Transfer t
        JOIN FETCH t.sender
        JOIN FETCH t.recipient
        ORDER BY t.createdAt DESC
        """,
    )
    fun findRecentTransfers(): List<Transfer>
}
