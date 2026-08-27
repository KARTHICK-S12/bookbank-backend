package com.example.bookbank.controller;

import com.example.bookbank.entity.Borrow;
import com.example.bookbank.service.BorrowService;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.Map;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<Borrow> borrowDetails(){

        return borrowService.getBorrowDetails();
    }

    @PostMapping
    public Borrow borrowBook(@RequestBody Map<String, Object> request, Authentication authentication) {

        Long userId= Long.valueOf(authentication.getName());
        Long bookId = Long.valueOf(request.get("bookId").toString());
        LocalDate dueDate = LocalDate.parse(request.get("dueDate").toString());

        return borrowService.borrowBook(
                userId,
                bookId,
                dueDate
        );
    }

    @PutMapping("/return/{borrowId}")
    public Borrow returnBook(@PathVariable Long borrowId,Authentication authentication){
        Long userId= Long.valueOf(authentication.getName());
        return borrowService.returnBook(
                borrowId,
                userId
        );
    }
}