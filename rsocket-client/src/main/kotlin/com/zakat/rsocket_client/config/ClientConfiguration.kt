package com.zakat.rsocket_client.config

import io.rsocket.frame.decoder.PayloadDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.util.MimeTypeUtils
import reactor.util.retry.Retry
import java.time.Duration

@Configuration
class ClientConfiguration {

    @Bean
    fun getRSocketRequester(builder: RSocketRequester.Builder): RSocketRequester =
        builder
            .rsocketStrategies(clientRSocketStrategies())
            .rsocketConnector { connector ->
                connector
                    .payloadDecoder(PayloadDecoder.ZERO_COPY)
                    .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
            }
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp("localhost", 7000)

    @Bean
    fun clientRSocketStrategies(): RSocketStrategies =
        RSocketStrategies.builder()
            .encoder(Jackson2JsonEncoder())
            .decoder(Jackson2JsonDecoder())
            .build()
}