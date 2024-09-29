package com.project.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import com.project.demo.models.Transaction;
import com.project.demo.services.TransactionService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1") 
public class TransactionsController {

    private TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<Void> processTransaction(@RequestBody Transaction transaction) {
        transactionService.processTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
}

