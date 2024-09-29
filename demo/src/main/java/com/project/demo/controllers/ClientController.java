package com.project.demo.controllers;

import com.project.demo.models.Limit;
import com.project.demo.models.Transaction;
import com.project.demo.services.LimitService;
import com.project.demo.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ClientController {

    private TransactionService transactionService;
    private LimitService limitService;

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

    @GetMapping("/transactions/limit_exceeded")
    public ResponseEntity<List<Transaction>> getTransactionsExceedingLimit() {
        List<Transaction> transactions = transactionService.getTransactionsExceedingLimit();
        return ResponseEntity.ok(transactions);
    }
}
