package com.example.booklibrary.controller;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.model.SuccessBookRate;
import com.example.booklibrary.service.AuthorService;
import com.example.booklibrary.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class AuthorReadController {

    AuthorService authorService;
    BookService bookService;

    public AuthorReadController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("books/{authorName}")
    public Set<Book> getAllBooksByAuthorName(@PathVariable(name="authorName") String authorName) {
        return bookService.getAllBooksByAuthorName(authorName);
    }

    @GetMapping("book/most-selling/{authorName}")
    public Book getMostSellingBookByAuthorName(@PathVariable(name="authorName") String authorName) {
        return bookService.getMostSellingBook(authorName);
    }

    @GetMapping("book/most-published/{authorName}")
    public Book getMostPublishedBook(@PathVariable(name="authorName") String authorName) {
        return bookService.getMostPublishedBook(authorName);
    }

    @GetMapping("books/most-selling/")
    public List<Book> getMostSellingBooks(@RequestParam(name="name") String authorName) {
        return bookService.getMostSellingBooksByPartName(authorName);
    }

    @GetMapping("books/most-published")
    public List<Book> getMostPublishedBooks(@RequestParam(name="name") String authorName) {
        return bookService.getMostPublishedBooksByPartName(authorName);
    }

    @GetMapping("books/most-successful")
    public Map<Book, SuccessBookRate> getMostSuccessfulBooksByAuthorName(
            @RequestParam(name="name") String authorName) {
        return bookService.getMostSuccessfulBooksByPartName(authorName);
    }

    @GetMapping("authors/most-successful")
    public String getMostSuccessfulAuthors() {
        return bookService.getMostSuccessfulAuthor();
    }
}
