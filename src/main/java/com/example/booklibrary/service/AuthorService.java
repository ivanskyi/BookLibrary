package com.example.booklibrary.service;

import com.example.booklibrary.dto.AuthorDto;
import com.example.booklibrary.dto.SuccessAuthorDto;
import com.example.booklibrary.dto.SuccessBookRateDto;
import com.example.booklibrary.entity.Author;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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


    public Set<Book> getAllBooksByAuthorName(String authorName) {
        return  authorRepository.findByAuthorName(authorName).getBooks();
    }

    public Book getMostSellingBook(String authorName) {
        Optional<Book> book =  authorRepository.findByAuthorName(authorName).getBooks().stream()
                .max(Comparator.comparing(Book::getSoldAmount));
        return book.orElseGet(Book::new);
    }

    public Book getMostPublishedBook(String authorName) {
        Optional<Author> author = Optional.of(authorRepository.findByAuthorName(authorName));
        Optional<Book> book = author.get().getBooks().stream().max(Comparator.comparing(Book::getPublishedAmount));
        return book.orElseThrow(() -> new RuntimeException("We don't have this book"));}

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

    public Map<Book, SuccessBookRateDto> getMostSuccessfulBooksByPartName(String authorName) {
        Map<Book, SuccessBookRateDto> successfulAuthor = new HashMap<>();
        for (Author author : authorRepository.findAllByAuthorNameContaining(authorName)) {
            for (Book book : author.getBooks()) {
                successfulAuthor.put(book, new SuccessBookRateDto(getBookSuccessRate(book)));
            }
        }
        return successfulAuthor;
    }

    public SuccessAuthorDto getMostSuccessfulAuthor() {
        List<Author> authors = authorRepository.findAll();
        Map<Author, SuccessAuthorDto> rateSuccessAllAuthors = new HashMap<>();
        for (Author author : authors) {
            double ratingOfAllBooksOfTheAuthor = 0;
            int numberOfBooks = 0;
            for (Book book : author.getBooks()) {
                numberOfBooks++;
                ratingOfAllBooksOfTheAuthor += getBookSuccessRate(book);
            }
            rateSuccessAllAuthors.put(author, new SuccessAuthorDto(ratingOfAllBooksOfTheAuthor / numberOfBooks));
        }
        String nameAuthorWhichHasMostBigRate = "none";
        double mostBigAuthorRate = 0.0;
        for (Author author : rateSuccessAllAuthors .keySet()) {
            if (rateSuccessAllAuthors.get(author).getRate() > mostBigAuthorRate) {
                nameAuthorWhichHasMostBigRate = author.getAuthorName();
                mostBigAuthorRate = rateSuccessAllAuthors.get(author).getRate();
            }
        }
        return new SuccessAuthorDto(nameAuthorWhichHasMostBigRate, mostBigAuthorRate);
    }

    public double getBookSuccessRate(Book book) {
        double rate = 0;
        return book.getSoldAmount() == 0 || book.getPublishedAmount() == 0 ? 0
                : Math.max((double) book.getSoldAmount() / (double) book.getPublishedAmount(), rate);
    }
}
