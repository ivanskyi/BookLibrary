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

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author create(AuthorDto authorDto) {
        Author author = new Author(authorDto.getId(),
                authorDto.getAuthorName(), authorDto.getBirthDate(),
                authorDto.getPhone(), authorDto.getEmail());
        authorRepository.save(author);
        Author authorWhichContaineBooks = authorRepository.findByAuthorName(author.getAuthorName());
        for (Book bookDto : authorDto.getBooks()) {
            Book book = new Book(bookDto.getBookName(), authorWhichContaineBooks.getId(),
                    bookDto.getPublishedAmount(), bookDto.getSoldAmount());
            authorWhichContaineBooks.addBook(book);
        }
        return authorRepository.save(authorWhichContaineBooks);
    }

    public Author update(Author author) {
        if (authorRepository.findById(author.getId()).isPresent()) {
            Author newAuthor = authorRepository.findById(author.getId()).get();
            newAuthor.setAuthorName(author.getAuthorName());
            newAuthor.setBirthDate(author.getBirthDate());
            newAuthor.setPhone(author.getPhone());
            newAuthor.setEmail(author.getEmail());
            return authorRepository.save(newAuthor);
        }
        return new Author();
    }

    public ResponseEntity<?> delete(Author author) {
        authorRepository.delete(author);
        if (authorRepository.findById(author.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
