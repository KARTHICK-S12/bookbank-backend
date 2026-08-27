package com.example.bookbank.service;

import com.example.bookbank.entity.Book;
import com.example.bookbank.entity.Borrow;
import com.example.bookbank.entity.User;
import com.example.bookbank.enums.BorrowStatus;
import com.example.bookbank.exception.BookNotFoundException;
import com.example.bookbank.exception.BookUnavailableException;
import com.example.bookbank.exception.BorrowNotFoundException;
import com.example.bookbank.exception.UserNotFoundException;
import com.example.bookbank.repository.BookRepository;
import com.example.bookbank.repository.BorrowRepository;
import com.example.bookbank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowService(
            BorrowRepository borrowRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {

        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<Borrow> getBorrowDetails(){

        return borrowRepository.findAll();

    }
    public Borrow borrowBook(
            Long userId,
            Long bookId,
            LocalDate dueDate) {

        // 1. Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // 2. Find the book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found"));

        // 3. Check availability
        if (book.getAvailableQuantity() <= 0) {
            throw new BookUnavailableException("Book is not available and RESERVATION option is yet to be released");
        }

        // 4. Create borrowing record
        Borrow borrow = new Borrow();

        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(dueDate);
        borrow.setStatus(BorrowStatus.BORROWED);

        // 5. Decrease available book quantity
        book.setAvailableQuantity(
                book.getAvailableQuantity() - 1
        );

        // 6. Save updated book
        bookRepository.save(book);

        // 7. Save borrowing record
        return borrowRepository.save(borrow);
    }

    public Borrow returnBook(Long borrowId ,Long userId) {

        // 1. Find the borrow record
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() ->
                        new BorrowNotFoundException("Borrow record not found"));

        // 2. Check if the book is already returned
        if (!borrow.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You are not allowed to return this borrow"
            );
        }
        if (borrow.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book is already returned");
        }

        // 3. Get the book
        Book book = borrow.getBook();

        // 4. Increase available quantity
        book.setAvailableQuantity(
                book.getAvailableQuantity() + 1
        );

        // 5. Save the updated book
        bookRepository.save(book);

        // 6. Set return date
        borrow.setReturnDate(LocalDate.now());

        // 7. Change status
        borrow.setStatus(BorrowStatus.RETURNED);

        // 8. Save the borrow record
        return borrowRepository.save(borrow);
    }
}
