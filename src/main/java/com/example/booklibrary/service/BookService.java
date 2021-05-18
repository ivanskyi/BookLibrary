package com.example.booklibrary.service;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.repository.AuthorRepository;
import com.example.booklibrary.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookDto bookDto) {
        Book book = new Book(bookDto.getAuthorId(),
                bookDto.getBookName(),
                bookDto.getAuthorId(),
                bookDto.getPublishedAmount(),
                bookDto.getSoldAmount());
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        if (bookRepository.findById(book.getId()).isPresent()) {
            Book updatedBook = bookRepository.findById(book.getId()).get();
            updatedBook.setBookName(book.getBookName());
            updatedBook.setSoldAmount(book.getSoldAmount());
            updatedBook.setPublishedAmount(book.getPublishedAmount());
            updatedBook.setAuthorId(book.getAuthorId());
            return bookRepository.save(updatedBook);
        }
        return new Book();
    }

    public ResponseEntity<?> deleteBook(Book book) {
        bookRepository.delete(book);
        return bookRepository.findById(book.getId()).isPresent()
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
