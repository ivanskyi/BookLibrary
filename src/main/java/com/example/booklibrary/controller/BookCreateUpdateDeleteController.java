package com.example.booklibrary.controller;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
public class BookCreateUpdateDeleteController {

    BookService bookService;

    public BookCreateUpdateDeleteController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("create")
    public Book create(@RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @PutMapping("update")
    public Book update(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @DeleteMapping("delete")
    public ResponseEntity delete(@RequestBody Book book) {
        return bookService.deleteBook(book);
    }
}
