package com.example.bookbank.controller;

import com.example.bookbank.entity.Book;
import com.example.bookbank.service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // GET book by ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // POST - Create a new book
    @PostMapping
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.createBook(book);
    }

    // PUT - Update a book
    @PutMapping("/{id}")
    public Book updateBook(
            @Valid
            @PathVariable Long id,
            @RequestBody Book bookDetails) {

        return bookService.updateBook(id, bookDetails);
    }

    // DELETE - Delete a book
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return "Book deleted successfully";
    }
}