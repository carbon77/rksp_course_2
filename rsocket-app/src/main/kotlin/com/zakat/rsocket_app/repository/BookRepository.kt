package com.zakat.rsocket_app.repository

import com.zakat.rsocket_app.model.Book
import org.springframework.data.mongodb.repository.MongoRepository

interface BookRepository : MongoRepository<Book, String>