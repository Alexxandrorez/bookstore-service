package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model){
        model.addAttribute("books",bookService.getAllBooks());
        return "books-list";
    }

    @GetMapping("/{name}")
    public String getBookByName(@PathVariable String name, Model model){
        model.addAttribute("book", bookService.getBookByName(name));
        return "book-details";
    }

    @PostMapping("/update")
    public String updateBookByName(@RequestParam String name, BookDTO book){
        bookService.updateBookByName(name,book);
        return "redirect:/books/all";
    }

    @PostMapping("/delete")
    public String deleteBookByName(@RequestParam String name){
        bookService.deleteBookByName(name);
        return "redirect:/books/all";
    }

    @PostMapping("/add")
    public String addBook(BookDTO book){
        bookService.addBook(book);
        return "redirect:/books/all";
    }
}
