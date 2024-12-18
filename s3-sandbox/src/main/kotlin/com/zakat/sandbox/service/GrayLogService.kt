package com.zakat.sandbox.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.Base64

@Service
class GrayLogService {
    private val graylogApiUrl = "http://graylog:9000/api/search/universal/relative"
    private val username = "admin"
    private val password = "zakat88"

    fun getLogs(): String {
        val url = "$graylogApiUrl?query=*&range=3600&fields=timestamp,message,source"
        val headers = HttpHeaders()

        val auth = "$username:$password"
        val encodedAuth = Base64.getEncoder().encodeToString(auth.toByteArray())
        headers.set("Accept", "text/csv")
        headers.set("Authorization", "Basic $encodedAuth")

        val entity = HttpEntity<String>(headers)
        val response = RestTemplate().exchange(url, HttpMethod.GET, entity, String::class.java)

        return response.body!!
    }
}
