package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romzhel.eshop.entities.Book;
import ru.romzhel.eshop.services.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/**")
public class BookRestController {
    private BookService bookService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable Optional<Long> id) {
        return bookService.getBookById(id.get());
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        book.setId(0L);
        book = bookService.saveBook(book);
        return book;
    }

    @PutMapping(path = "/books", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Book updateBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/books/{id}")
    public int deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return HttpStatus.OK.value();
    }
}
