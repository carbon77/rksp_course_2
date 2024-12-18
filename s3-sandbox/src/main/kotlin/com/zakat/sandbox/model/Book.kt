package com.zakat.sandbox.model

import jakarta.persistence.*

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "book_id")
    var id: Long?=null,
    var title: String = "",
    var rating: Double = 0.0,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "author_id")
    var author: Author? = null,

    @ManyToMany
    @JoinTable(
        name = "books_genres",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    var genres: MutableSet<Genre> = mutableSetOf()
)
