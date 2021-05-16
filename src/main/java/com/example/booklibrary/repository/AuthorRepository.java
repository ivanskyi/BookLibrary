package com.example.booklibrary.repository;

import com.example.booklibrary.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByAuthorName(String authorName);
    List<Author> findAllByAuthorNameContaining(String partName);
}
