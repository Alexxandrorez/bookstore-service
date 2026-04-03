package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/edit/{name}")
    public String showEditForm(@PathVariable String name, Model model) {
        BookDTO book = bookService.getBookByName(name);
        model.addAttribute("book", book);
        model.addAttribute("oldName", name);
        return "edit-book";
    }

    @PostMapping("/update")
    public String updateBook(@RequestParam("oldName") String oldName,
                             @Valid @ModelAttribute("book") BookDTO bookDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("oldName", oldName);
            return "edit-book";
        }

        try {
            bookService.updateBookByName(oldName, bookDTO);
            return "redirect:/employee/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Помилка: " + e.getMessage());
            model.addAttribute("oldName", oldName);
            return "edit-book";
        }
    }

    @GetMapping("/all")
    public String getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {
        int pageSize = 12;
        Page<BookDTO> bookPage = bookService.getAllBooks(page, pageSize, sortBy, direction);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "books-list";
    }

    @GetMapping("/details/{name}")
    public String getBookByName(@PathVariable String name, Model model) {
        model.addAttribute("book", bookService.getBookByName(name));
        return "book-details";
    }

    @PostMapping("/delete")
    public String deleteBookByName(@RequestParam String name) {
        bookService.deleteBookByName(name);
        return "redirect:/employee/dashboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "add-book";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") BookDTO bookDTO,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "add-book";
        }

        try {
            bookService.addBook(bookDTO);
            return "redirect:/employee/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Не вдалося додати книгу: " + e.getMessage());
            return "add-book";
        }
    }
}