package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.romzhel.eshop.entities.Book;
import ru.romzhel.eshop.services.BookService;

@Controller
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("booksList", bookService.getAllBooks());
        return "books-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "add-book-form";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String refreshList(Model model, Book book) {
        bookService.saveBook(book);
        return "redirect:/books/list";
    }
}
