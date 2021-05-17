package com.example.booklibrary.service;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.entity.Author;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.model.SuccessAuthor;
import com.example.booklibrary.model.SuccessAuthorRate;
import com.example.booklibrary.model.SuccessBookRate;
import com.example.booklibrary.repository.AuthorRepository;
import com.example.booklibrary.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
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

    public Set<Book> getAllBooksByAuthorName(String authorName) {
        return  authorRepository.findByAuthorName(authorName).getBooks();
    }

    public Book getMostSellingBook(String authorName) {
        Optional<Book> book =  authorRepository.findByAuthorName(authorName).getBooks().stream()
                .max(Comparator.comparing(Book::getSoldAmount));
        return book.orElseGet(Book::new);
    }

    public Book getMostPublishedBook(String authorName) {
        Optional<Book> book =  authorRepository.findByAuthorName(authorName).getBooks().stream()
                .max(Comparator.comparing(Book::getPublishedAmount));
        return book.orElseGet(Book::new);
    }

    public List<Book> getMostSellingBooksByPartName(String authorName) {
        return  authorRepository.findAllByAuthorNameContaining(authorName).stream()
                .map(Author::getBooks)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Book::getSoldAmount))
                .sorted((a, b) -> Long.compare(b.getSoldAmount(), a.getSoldAmount()))
                .collect(Collectors.toList());
    }

    public List<Book> getMostPublishedBooksByPartName(String authorName) {
        return authorRepository.findAllByAuthorNameContaining(authorName).stream()
                .map(Author::getBooks)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Book::getPublishedAmount))
                .sorted((a, b) -> Long.compare(b.getPublishedAmount(), a.getPublishedAmount()))
                .collect(Collectors.toList());
    }

    public Map<Book, SuccessBookRate> getMostSuccessfulBooksByPartName(String authorName) {
        Map<Book, SuccessBookRate> successfulAuthor = new HashMap<>();
        for (Author author : authorRepository.findAllByAuthorNameContaining(authorName)) {
            for (Book book : author.getBooks()) {
                successfulAuthor.put(book, new SuccessBookRate(getBookSuccessRate(book)));
            }
        }
        return successfulAuthor;
    }

    public SuccessAuthor getMostSuccessfulAuthor() {
        List<Author> authors = authorRepository.findAll();
        Map<Author, SuccessAuthorRate> rateSuccessAllAuthors = new HashMap<>();
        for (Author author : authors) {
            double ratingOfAllBooksOfTheAuthor = 0;
            int numberOfBooks = 0;
            for (Book book : author.getBooks()) {
                numberOfBooks++;
                ratingOfAllBooksOfTheAuthor += getBookSuccessRate(book);
            }
            rateSuccessAllAuthors.put(author, new SuccessAuthorRate(ratingOfAllBooksOfTheAuthor / numberOfBooks));
        }
        String nameAuthorWhichHasMostBigRate = "none";
        double mostBigAuthorRate = 0.0;
        for (Author author : rateSuccessAllAuthors .keySet()) {
            if (rateSuccessAllAuthors.get(author).getRate() > mostBigAuthorRate) {
                nameAuthorWhichHasMostBigRate = author.getAuthorName();
                mostBigAuthorRate = rateSuccessAllAuthors.get(author).getRate();
            }
        }
        return new SuccessAuthor(nameAuthorWhichHasMostBigRate, mostBigAuthorRate);
    }

    public double getBookSuccessRate(Book book) {
        double rate = 0;
        return book.getSoldAmount() == 0 || book.getPublishedAmount() == 0 ? 0
                : Math.max((double) book.getSoldAmount() / (double) book.getPublishedAmount(), rate);
    }
}
