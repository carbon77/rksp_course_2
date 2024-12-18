package ru.zakat.books_webflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.zakat.books_webflux.model.Book;
import ru.zakat.books_webflux.service.BookService;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public Mono<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found")));
    }

    @GetMapping("/error/{id}")
    public Mono<Book> getBookByIdError(@PathVariable Long id) {
        return bookService
                .getBookById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Book not found")))
                .onErrorResume(e -> Mono.just(new Book(1L, "Unknown", "Unknown", 0.0)));
    }

    @GetMapping
    public Flux<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public Mono<Book> createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    public Mono<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @GetMapping("/rated")
    public Flux<Book> findByRating(@RequestParam Double rating) {
        return bookService.getAllBooks()
                .filter(book -> book.getRating() >= rating)
                .onBackpressureBuffer();
    }
}
