package ru.zakat.books_webflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.zakat.books_webflux.model.Book;
import ru.zakat.books_webflux.repository.BooksRepository;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BooksRepository booksRepository;

    public Mono<Book> getBookById(Long id) {
        return booksRepository.findById(id);
    }

    public Flux<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    public Mono<Book> createBook(Book book) {
        return booksRepository.save(book);
    }

    public Mono<Void> deleteBook(Long id) {
        return booksRepository.deleteById(id);
    }

    public Mono<Book> updateBook(Long id, Book newBook) {
        return booksRepository
                .findById(id)
                .flatMap(book -> {
                    book.setAuthor(newBook.getAuthor());
                    book.setTitle(newBook.getTitle());
                    book.setRating(newBook.getRating());
                    return booksRepository.save(book);
                });
    }
}
