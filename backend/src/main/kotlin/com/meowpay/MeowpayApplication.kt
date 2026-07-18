package com.meowpay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MeowpayApplication

fun main(args: Array<String>) {
    runApplication<MeowpayApplication>(*args)
}
