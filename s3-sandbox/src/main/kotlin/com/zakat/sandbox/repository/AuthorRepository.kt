package com.zakat.sandbox.repository;

import com.zakat.sandbox.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
interface AuthorRepository : JpaRepository<Author, Long>