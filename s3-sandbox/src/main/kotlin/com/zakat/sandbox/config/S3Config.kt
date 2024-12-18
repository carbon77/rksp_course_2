package com.zakat.sandbox.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(
    private val s3ConfigProperties: S3ConfigProperties,
) {
    @Bean
    fun awsCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(
            s3ConfigProperties.awsAccessKeyId,
            s3ConfigProperties.awsSecretAccessKey
        )
    }

    @Bean
    fun s3Client(): AmazonS3 =
        AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials()))
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    s3ConfigProperties.endpoint,
                    s3ConfigProperties.region,
                )
            )
            .build()
}