package ru.zakat.books_webflux;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.zakat.books_webflux.controller.BookController;
import ru.zakat.books_webflux.model.Book;
import ru.zakat.books_webflux.service.BookService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @Test
    public void testGetBookById() {
        Book book = new Book(1L, "Author", "Title", 4.5);
        when(bookService.getBookById(1L)).thenReturn(Mono.just(book));

        webTestClient.get().uri("/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book(1L, "Author1", "Title1", 4.5);
        Book book2 = new Book(2L, "Author2", "Title2", 3.8);
        when(bookService.getAllBooks()).thenReturn(Flux.just(book1, book2));

        webTestClient.get().uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    public void testCreateBook() {
        Book book = new Book(null, "New Author", "New Title", 5.0);
        Book savedBook = new Book(1L, "New Author", "New Title", 5.0);

        when(bookService.createBook(any(Book.class))).thenReturn(Mono.just(savedBook));

        webTestClient.post().uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(savedBook);
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book(1L, "Old Author", "Old Title", 3.5);
        Book updatedBook = new Book(1L, "Updated Author", "Updated Title", 4.0);

        when(bookService.getBookById(1L)).thenReturn(Mono.just(existingBook));
        when(bookService.updateBook(1L, updatedBook)).thenReturn(Mono.just(updatedBook));

        webTestClient.put().uri("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBook)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(updatedBook);
    }

    @Test
    public void testDeleteBook() {
        when(bookService.deleteBook(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/books/1")
                .exchange()
                .expectStatus().isOk();

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookService.getBookById(1L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/books/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetBookWithErrorHandling() {
        when(bookService.getBookById(1L)).thenReturn(Mono.error(new RuntimeException("Book not found")));

        webTestClient.get().uri("/books/error/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(book -> {
                    assertEquals("Unknown", book.getAuthor());
                    assertEquals("Unknown", book.getTitle());
                    assertEquals(0.0, book.getRating());
                });
    }

    @Test
    public void testGetHighRatedBooks() {
        Book book1 = new Book(1L, "Author1", "Title1", 4.5);
        Book book2 = new Book(2L, "Author2", "Title2", 3.0);
        Book book3 = new Book(3L, "Author3", "Title3", 5.0);

        when(bookService.getAllBooks()).thenReturn(Flux.just(book1, book2, book3));

        webTestClient.get().uri("/books/rated?rating=4")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .hasSize(2)
                .contains(book1, book3);
    }
}
