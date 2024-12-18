package com.zakat.rsocket_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RsocketAppApplication

fun main(args: Array<String>) {
	runApplication<RsocketAppApplication>(*args)
}
