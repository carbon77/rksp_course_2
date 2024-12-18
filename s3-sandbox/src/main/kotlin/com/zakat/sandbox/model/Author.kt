package com.zakat.sandbox.model

import jakarta.persistence.*

@Entity
@Table(name = "authors")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "author_id")
    var id: Long? = null,

    var firstName: String = "",
    var lastName: String = "",

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    var books: MutableList<Book> = mutableListOf()
)
