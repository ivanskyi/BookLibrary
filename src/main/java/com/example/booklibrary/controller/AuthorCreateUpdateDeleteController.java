package com.example.booklibrary.controller;

import com.example.booklibrary.dto.AuthorDto;
import com.example.booklibrary.entity.Author;
import com.example.booklibrary.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("author")
public class AuthorCreateUpdateDeleteController {

    AuthorService authorService;

    public AuthorCreateUpdateDeleteController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("create")
    public Author create(@RequestBody AuthorDto authorDto) {
        return authorService.create(authorDto);
    }

    @PutMapping("update")
    public Author update(@RequestBody  Author author) {
        return authorService.update(author);
    }

    @DeleteMapping("delete")
    public ResponseEntity delete(@RequestBody Author author) {
        return authorService.delete(author);
    }
}
