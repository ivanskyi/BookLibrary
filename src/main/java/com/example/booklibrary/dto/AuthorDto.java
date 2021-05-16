package com.example.booklibrary.dto;

import com.example.booklibrary.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthorDto {
    private Long  id;
    private String authorName;
    private LocalDateTime birthDate;
    private String phone;
    private String email;
    private Set<Book> books = new HashSet<>();
}
