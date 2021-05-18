package com.example.booklibrary.repository;

import com.example.booklibrary.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByAuthorName(String authorName);

    @Query("FROM Author u WHERE u.authorName  LIKE CONCAT('%', :partName)")
    List<Author> findAllByAuthorNameContaining(@Param("partName") String partName);

}
