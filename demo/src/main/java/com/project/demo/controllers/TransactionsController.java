package com.project.demo.controllers;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import com.project.demo.models.Transaction;
import com.project.demo.services.TransactionService;

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
}

