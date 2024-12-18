package com.zakat.rsocket_app.controller

import com.zakat.rsocket_app.model.Book
import com.zakat.rsocket_app.repository.BookRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class BookController(
    val bookRepository: BookRepository,
) {

    @MessageMapping("getBooks")
    fun getBooks(): Flux<Book> {
        return Flux.fromIterable(bookRepository.findAll())
    }

    @MessageMapping("getBook")
    fun getBook(id: String): Mono<Book> {
        return Mono.justOrEmpty(bookRepository.findById(id))
    }

    @MessageMapping("deleteBook")
    fun deleteBook(id: String): Mono<Void> {
        bookRepository.deleteById(id)
        return Mono.empty()
    }

    @MessageMapping("addBook")
    fun addBook(book: Book): Mono<Book> {
        return Mono.justOrEmpty(bookRepository.save(book))
    }

    @MessageMapping("bookChannel")
    fun bookChannel(books: Flux<Book>): Flux<Book> {
        return books
            .flatMap { book ->
                Thread.sleep(1000)
                Mono.fromCallable { bookRepository.save(book) }
            }
            .collectList()
            .flatMapMany { savedBooks -> Flux.fromIterable(savedBooks) }
    }
}