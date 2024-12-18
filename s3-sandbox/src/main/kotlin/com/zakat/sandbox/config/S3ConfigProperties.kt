package com.zakat.sandbox.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.s3")
data class S3ConfigProperties(
    var awsAccessKeyId: String = "",
    var awsSecretAccessKey: String = "",
    var endpoint: String = "",
    var region: String = "",
)