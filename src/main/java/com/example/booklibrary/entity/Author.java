package com.example.booklibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorName;
    private LocalDateTime birthDate;
    private String phone;
    private String email;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public Author(Long id, String authorName, LocalDateTime birthDate, String phone, String email) {
        this.id = id;
        this.authorName = authorName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
    }
}
