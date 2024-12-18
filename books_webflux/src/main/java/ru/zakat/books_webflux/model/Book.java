package ru.zakat.books_webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("books")
public class Book {

    @Id
    @Column("book_id")
    private Long id;

    private String author;
    private String title;
    private Double rating;
}
