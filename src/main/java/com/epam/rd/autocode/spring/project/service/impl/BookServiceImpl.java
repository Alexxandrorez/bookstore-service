package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();
    }

    @Override
    public BookDTO updateBookByName(String name, BookDTO book) {
        Book bookEnt=bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Not found"));
        modelMapper.map(book, bookEnt);

        Book savedBook = bookRepository.save(bookEnt);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    public void deleteBookByName(String name) {
        Book book=bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Not found"));
        bookRepository.delete(book);
    }

    @Override
    public BookDTO addBook(BookDTO book) {
        Book bookEnt = modelMapper.map(book, Book.class);
        bookRepository.save(bookEnt);
        return modelMapper.map(bookEnt, BookDTO.class);
    }

    @Override
    public BookDTO getBookByName(String name) {
        return bookRepository.findByName(name)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(()->new NotFoundException("Not found"));
    }
}
