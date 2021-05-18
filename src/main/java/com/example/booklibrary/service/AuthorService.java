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
        Author authorFromDB = authorRepository.findByAuthorName(author.getAuthorName());
        for (Book bookDto : authorDto.getBooks()) {
            Book book = new Book(bookDto.getBookName(), authorFromDB.getId(),
                    bookDto.getPublishedAmount(), bookDto.getSoldAmount());
            authorFromDB.addBook(book);
        }
        return authorRepository.save(authorFromDB);
    }

    public Author update(Author author) {
        if (authorRepository.findById(author.getId()).isPresent()) {
            Author newAuthor = authorRepository.findById(author.getId()).get();
            newAuthor.setAuthorName(author.getAuthorName());
            newAuthor.setBirthDate(author.getBirthDate());
            newAuthor.setPhone(author.getPhone());
            newAuthor.setEmail(author.getEmail());
            return authorRepository.save(newAuthor);
        } else {
            throw new RuntimeException("We didn't update this author, because we didn't find him.");
        }
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
        Optional<Author> author = Optional.ofNullable(authorRepository.findByAuthorName(authorName));
        if (author.isPresent()) {
            return author.get().getBooks();
        } else {
            throw new RuntimeException("We didn't find this author and all him books.");
        }
    }

    public Book getMostSellingBook(String authorName) {
        Optional<Book> mostSellingBook = Optional.empty();
        mostSellingBook = getBook(authorName, mostSellingBook, Comparator.comparing(Book::getSoldAmount));
        if (mostSellingBook.isPresent()) {
            return mostSellingBook.get();
        } else {
            throw new RuntimeException("We didn't find this author and most selling book.");
        }
    }

    private Optional<Book> getBook(String authorName, Optional<Book> mostSellingBook, Comparator<Book> comparing) {
        Optional<Author> author = Optional.ofNullable(authorRepository.findByAuthorName(authorName));
        if (author.isPresent()) {
            Optional<Set<Book>> book = Optional.ofNullable(author.get().getBooks());
            if (book.isPresent()) {
                mostSellingBook = book.get().stream()
                        .max(comparing);
            }
        }
        return mostSellingBook;
    }

    public Book getMostPublishedBook(String authorName) {
        Optional<Book> mostPublishedBook = Optional.empty();
        mostPublishedBook = getBook(authorName, mostPublishedBook, Comparator.comparing(Book::getPublishedAmount));
        if (mostPublishedBook.isPresent()) {
            return mostPublishedBook.get();
        } else {
            throw new RuntimeException("We didn't find this author and most published book.");
        }
    }

    public List<Book> getMostSellingBooksByPartName(String authorName) {
        List<Book> books = authorRepository.findAllByAuthorNameContaining(authorName).stream()
                .map(Author::getBooks)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Book::getSoldAmount))
                .sorted((a, b) -> Long.compare(b.getSoldAmount(), a.getSoldAmount()))
                .collect(Collectors.toList());
        if (!books.isEmpty()) {
            return books;
        } else {
            throw new RuntimeException("We didn't find any most selling book by part author name.");
        }
    }

    public List<Book> getMostPublishedBooksByPartName(String authorName) {
        List<Book> books =  authorRepository.findAllByAuthorNameContaining(authorName).stream()
                .map(Author::getBooks)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Book::getPublishedAmount))
                .sorted((a, b) -> Long.compare(b.getPublishedAmount(), a.getPublishedAmount()))
                .collect(Collectors.toList());
        if (!books.isEmpty()) {
            return books;
        } else {
            throw new RuntimeException("We didn't find any most published book by part author name.");
        }
    }

    public Map<Book, SuccessBookRateDto> getMostSuccessfulBooksByPartName(String authorName) {
        Map<Book, SuccessBookRateDto> noSortedMostSuccessfulBooks = new HashMap<>();
        for (Author author : authorRepository.findAllByAuthorNameContaining(authorName)) {
            for (Book book : author.getBooks()) {
                noSortedMostSuccessfulBooks.put(book, new SuccessBookRateDto(getBookSuccessRate(book)));
            }
        }
        Map<Book, SuccessBookRateDto> sortedMostSuccessfulBooks = new LinkedHashMap<>();
        noSortedMostSuccessfulBooks.entrySet().stream()
                .sorted(Map.Entry.<Book, SuccessBookRateDto>comparingByValue().reversed())
                .forEach(a -> sortedMostSuccessfulBooks.put(a.getKey(), a.getValue()));
        if (!sortedMostSuccessfulBooks.isEmpty()) {
            return sortedMostSuccessfulBooks;
        } else {
            throw new RuntimeException("We didn't find any successful book use this name.");
        }
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
        String nameAuthorWhichHasMostBigRate = null;
        double mostBigAuthorRate = 0.0;
        for (Author author : rateSuccessAllAuthors .keySet()) {
            if (rateSuccessAllAuthors.get(author).getRate() > mostBigAuthorRate) {
                nameAuthorWhichHasMostBigRate = author.getAuthorName();
                mostBigAuthorRate = rateSuccessAllAuthors.get(author).getRate();
            }
        }
        if (nameAuthorWhichHasMostBigRate != null) {
            return new SuccessAuthorDto(nameAuthorWhichHasMostBigRate, mostBigAuthorRate);
        } else {
            throw new RuntimeException("We didn't find any successful author.");
        }
    }

    public double getBookSuccessRate(Book book) {
        double rate = 0;
        return book.getSoldAmount() == 0 || book.getPublishedAmount() == 0 ? 0
                : Math.max((double) book.getSoldAmount() / (double) book.getPublishedAmount(), rate);
    }
}
