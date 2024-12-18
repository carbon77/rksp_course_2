package com.zakat.rsocket_client.controller

import com.zakat.rsocket_client.model.Book
import org.reactivestreams.Publisher
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlux
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/books")
class BookController(
    val rSocketRequester: RSocketRequester
) {

    @GetMapping
    fun getBooks(): Publisher<Book> {
        return rSocketRequester
            .route("getBooks")
            .retrieveFlux()
    }

    @GetMapping("/{bookId}")
    fun getBook(@PathVariable bookId: String): Mono<Book> {
        return rSocketRequester
            .route("getBook")
            .data(bookId)
            .retrieveMono()
    }

    @PostMapping
    fun addBook(@RequestBody book: Book): Mono<Book> {
        return rSocketRequester
            .route("addBook")
            .data(book)
            .retrieveMono()
    }

    @DeleteMapping("/{bookId}")
    fun deleteBook(@PathVariable bookId: String): Publisher<Void> {
        return rSocketRequester
            .route("deleteBook")
            .data(bookId)
            .send()
    }

    @PostMapping("/addBooks")
    fun addBooks(@RequestBody books: List<Book>): Flux<Book> {
        return rSocketRequester
            .route("bookChannel")
            .data(books)
            .retrieveFlux()
    }
}