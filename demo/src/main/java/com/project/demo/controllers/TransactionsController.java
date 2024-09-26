package com.project.demo.controllers;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import com.project.demo.models.Limit;
import com.project.demo.models.Transaction;
import com.project.demo.services.LimitService;
import com.project.demo.services.TransactionService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1") 
public class TransactionsController {

    private TransactionService transactionService;
    private LimitService limitService;

    @PostMapping("/transaction")
    public ResponseEntity<Void> processTransaction(@RequestBody Transaction transaction) {
        transactionService.processTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/set_limit")
    public ResponseEntity<Void> setLimit(@RequestBody Limit limit) {
        limitService.setLimit(limit);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/limits")
    public ResponseEntity<List<Limit>> getAllLimits() {
        List<Limit> limits = limitService.getAllLimits();
        return ResponseEntity.ok(limits);
    }

    @GetMapping("/transactions/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory(@PathVariable String category) {
        List<Transaction> transactions = transactionService.getTransactionsByCategory(category);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/currency/{currency}")
    public ResponseEntity<List<Transaction>> getTransactionsByCurrency(@PathVariable String currency) {
        List<Transaction> transactions =  transactionService.getTransactionsByCurrency(currency);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/limit_exceeded")
    public ResponseEntity<List<Transaction>> getTransactionsExceedingLimit() {
        List<Transaction> transactions = transactionService.getTransactionsExceedingLimit();
        return ResponseEntity.ok(transactions);
    }
}

