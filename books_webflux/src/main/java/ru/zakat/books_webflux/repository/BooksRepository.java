package ru.zakat.books_webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.zakat.books_webflux.model.Book;

public interface BooksRepository extends ReactiveCrudRepository<Book, Long> {
}
