package com.zakat.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter

@SpringBootApplication
class SandboxApplication {
    @Bean
    fun loggingFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeClientInfo(true)
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(64000)
        return filter
    }
}

fun main(args: Array<String>) {
    runApplication<SandboxApplication>(*args)
}
