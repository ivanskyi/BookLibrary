package com.example.booklibrary.controller;

import com.example.booklibrary.dto.AuthorDto;
import com.example.booklibrary.dto.SuccessAuthorDto;
import com.example.booklibrary.dto.SuccessBookRateDto;
import com.example.booklibrary.entity.Author;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public Author create(@RequestBody AuthorDto authorDto) {
        return authorService.create(authorDto);
    }

    @PutMapping
    public Author update(@RequestBody  Author author) {
        return authorService.update(author);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Author author) {
        return authorService.delete(author);
    }

    @GetMapping("all-books/{authorName}")
    public Set<Book> getAllBooksByAuthorName(@PathVariable(name = "authorName") String authorName) {
        return authorService.getAllBooksByAuthorName(authorName);
    }

    @GetMapping("most-selling-book/{authorName}")
    public Book getMostSellingBookByAuthorName(@PathVariable(name = "authorName") String authorName) {
        return authorService.getMostSellingBook(authorName);
    }

    @GetMapping("most-published-book/{authorName}")
    public Book getMostPublishedBook(@PathVariable(name = "authorName") String authorName) {
        return authorService.getMostPublishedBook(authorName);
    }

    @GetMapping("most-selling-books")
    public List<Book> getMostSellingBooks(@RequestParam(name = "name") String authorName) {
        return authorService.getMostSellingBooksByPartName(authorName);
    }

    @GetMapping("most-published-books")
    public List<Book> getMostPublishedBooks(@RequestParam(name = "name") String authorName) {
        return authorService.getMostPublishedBooksByPartName(authorName);
    }

    @GetMapping("most-successful-books")
    public Map<Book, SuccessBookRateDto> getMostSuccessfulBooksByAuthorName(
            @RequestParam(name = "name") String authorName) {
        return authorService.getMostSuccessfulBooksByPartName(authorName);
    }

    @GetMapping("most-successful")
    public SuccessAuthorDto getMostSuccessfulAuthors() {
        return authorService.getMostSuccessfulAuthor();
    }
}
