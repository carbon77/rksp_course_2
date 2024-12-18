package com.zakat.rsocket_app

import com.zakat.rsocket_app.model.Book
import com.zakat.rsocket_app.repository.BookRepository
import io.rsocket.frame.decoder.PayloadDecoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlux
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.util.MimeTypeUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import kotlin.jvm.optionals.getOrNull
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsocketAppApplicationTests {
	@Autowired
	private lateinit var bookRepository: BookRepository
	private lateinit var requester: RSocketRequester

	@BeforeEach
	fun setup() {
		requester = RSocketRequester.builder()
			.rsocketStrategies { builder -> builder
				.decoder(Jackson2JsonDecoder())
			}
			.rsocketStrategies { builder -> builder
				.encoder(Jackson2JsonEncoder())
			}
			.rsocketConnector { connector -> connector
				.payloadDecoder(PayloadDecoder.ZERO_COPY)
				.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
			}
			.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
			.tcp("localhost", 7000)
	}

	@AfterEach
	fun cleanUp() {
		requester.dispose()
	}

	@Test
	fun testGetBook_returnsBook() {
		val book = Book(
			id = null,
			title = "Book",
			publishYear = 2000,
			rating = 3.5
		)

		val savedBook = bookRepository.save(book)
		val result: Mono<Book> = requester
			.route("getBook")
			.data(savedBook.id!!)
			.retrieveMono()

		assertNotNull(result.block())
	}

	@Test
	fun testAddBook_returnsNewBook() {
		val book = Book(
			id = null,
			title = "Book",
			publishYear = 2000,
			rating = 2.3
		)

		val result: Mono<Book> = requester
			.route("addBook")
			.data(book)
			.retrieveMono()

		val savedBook = result.block()
		assertNotNull(savedBook)
		assertNotNull(savedBook.id)
		assertTrue(savedBook.id!!.isNotEmpty())
	}

	@Test
	fun testGetCats() {
		val result: Flux<Book> = requester
			.route("getBooks")
			.retrieveFlux()
		assertNotNull(result.blockFirst())
	}

	@Test
	fun testDeleteCat() {
		val book = Book(
			id = null,
			title = "Book",
			publishYear = 2000,
			rating = 3.5
		)

		val savedBook = bookRepository.save(book)
		val result: Mono<Void> = requester
			.route("deleteBook")
			.data(savedBook.id!!)
			.retrieveMono()

		result.block()
		val deletedBook = bookRepository.findById(savedBook.id!!).getOrNull()
		assertNotSame(savedBook, deletedBook)
	}


}
