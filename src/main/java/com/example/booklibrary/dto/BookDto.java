package com.example.booklibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class BookDto {
    private String bookName;
    private Long authorId;
    private Long publishedAmount;
    private Long soldAmount;
}