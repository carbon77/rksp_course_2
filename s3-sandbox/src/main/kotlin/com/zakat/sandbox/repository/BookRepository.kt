package com.zakat.sandbox.repository

import com.zakat.sandbox.model.Book
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "books", path = "books")
interface BookRepository : CrudRepository<Book, Long>
