package com.zakat.rsocket_app.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("books")
data class Book(
    @Id
    val id: String?,
    val title: String,
    val publishYear: Int,
    val rating: Double,
)
