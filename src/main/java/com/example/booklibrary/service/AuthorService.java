package com.example.booklibrary.service;

import com.example.booklibrary.dto.AuthorDto;
import com.example.booklibrary.entity.Author;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author create(AuthorDto authorDto) {
        Author author = new Author(authorDto.getId(),
                authorDto.getAuthorName(),authorDto.getBirthDate(),
                authorDto.getPhone(), authorDto.getEmail());

        for (Book bookDto : authorDto.getBooks()) {
            Book book = new Book(bookDto.getAuthorId(),
                    bookDto.getBookName(), bookDto.getAuthorId(),
                    bookDto.getPublishedAmount(), bookDto.getSoldAmount());
            author.addBook(book);
        }
        return authorRepository.save(author);
    }

    public Author update(Author author) {
        Author newAuthor  = authorRepository.findById(author.getId()).get();
        newAuthor.setAuthorName(author.getAuthorName());
        newAuthor.setBirthDate(author.getBirthDate());
        newAuthor.setPhone(author.getPhone());
        newAuthor.setEmail(author.getEmail());
        return authorRepository.save(newAuthor);
    }

    public ResponseEntity delete(Author author) {
        authorRepository.delete(author);
        if (authorRepository.findById(author.getId()).isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
