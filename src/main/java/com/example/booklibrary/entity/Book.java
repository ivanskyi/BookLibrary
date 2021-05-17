package com.example.booklibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private Long authorId;
    private Long publishedAmount;
    private Long soldAmount;

    public Book(String bookName, Long authorId, Long publishedAmount, Long soldAmount) {
        this.bookName = bookName;
        this.authorId = authorId;
        this.publishedAmount = publishedAmount;
        this.soldAmount = soldAmount;
    }
}