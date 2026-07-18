package com.meowpay.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.meowpay.dto.request.CreateTransferRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TransferIntegrationTest {

    companion object {
        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("meowpay")
            .withUsername("meowpay")
            .withPassword("meowpay")

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }

        private val WHISKERS_ID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        private val MITTENS_ID = UUID.fromString("22222222-2222-2222-2222-222222222222")
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `lists seeded cats`() {
        mockMvc.get("/api/cats")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].name") { exists() }
                jsonPath("$[0].balance") { exists() }
            }
    }

    @Test
    fun `creates transfer and updates balances`() {
        val initialBalance = objectMapper.readTree(
            mockMvc.get("/api/cats/$WHISKERS_ID").andReturn().response.contentAsString,
        ).get("balance").decimalValue()

        val request = CreateTransferRequest(
            senderId = WHISKERS_ID,
            recipientId = MITTENS_ID,
            amount = BigDecimal("10.00"),
        )

        mockMvc.post("/api/transfers") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.amount") { value(10.0) }
            jsonPath("$.sender.name") { value("Whiskers") }
            jsonPath("$.recipient.name") { value("Mittens") }
        }

        val updatedBalance = objectMapper.readTree(
            mockMvc.get("/api/cats/$WHISKERS_ID").andReturn().response.contentAsString,
        ).get("balance").decimalValue()

        assertEquals(initialBalance.subtract(BigDecimal("10.00")), updatedBalance)
    }

    @Test
    fun `rejects transfer when balance is insufficient`() {
        val request = CreateTransferRequest(
            senderId = UUID.fromString("33333333-3333-3333-3333-333333333333"),
            recipientId = WHISKERS_ID,
            amount = BigDecimal("1000.00"),
        )

        mockMvc.post("/api/transfers") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isUnprocessableEntity() }
            jsonPath("$.message") { exists() }
        }
    }

    @Test
    fun `rejects self transfer`() {
        val request = CreateTransferRequest(
            senderId = WHISKERS_ID,
            recipientId = WHISKERS_ID,
            amount = BigDecimal("1.00"),
        )

        mockMvc.post("/api/transfers") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
