package com.example.bookbank.service;

import com.example.bookbank.entity.Book;
import com.example.bookbank.exception.BookNotFoundException;
import com.example.bookbank.exception.BookValidationException;
import com.example.bookbank.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    public Book getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        return book;
    }

    // Add a new book
    public Book createBook(Book book) {

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookValidationException("ISBN already exists");
        }

        if (book.getAvailableQuantity() > book.getTotalQuantity()) {
            throw new BookValidationException(
                    "Available quantity cannot be greater than total quantity"
            );
        }

        return bookRepository.save(book);
    }

    // Update an existing book
    public Book updateBook(Long id, Book bookDetails) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setCategory(bookDetails.getCategory());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setPublicationYear(bookDetails.getPublicationYear());
        existingBook.setTotalQuantity(bookDetails.getTotalQuantity());
        existingBook.setAvailableQuantity(bookDetails.getAvailableQuantity());

        return bookRepository.save(existingBook);
    }

    // Delete a book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}