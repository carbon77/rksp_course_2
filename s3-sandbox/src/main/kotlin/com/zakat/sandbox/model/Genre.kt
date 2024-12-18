package com.zakat.sandbox.model

import jakarta.persistence.*

@Entity
@Table(name = "genres")
data class Genre(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "genre_id")
    val id: Long? = null,
    val name: String = "",
)
