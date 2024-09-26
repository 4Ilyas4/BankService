package com.project.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.demo.models.Transaction;
import com.project.demo.repositories.TransactionsRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class TransactionService {

    private LimitService limitService;

    private TransactionsRepository transactionRepository;

    public void processTransaction(Transaction transaction) {
        BigDecimal totalSum = getLastTransaction().getTotalSum().add(transaction.getSum());
        transaction.setTotalSum(totalSum);
        BigDecimal limit = limitService.getLastLimit().getLimitSum();
        if(totalSum.compareTo(limit) > 0){
            transaction.setLimitExceeded(true);
        }
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        return transactionRepository.findByExpenseCategory(category);
    }

    public List<Transaction> getTransactionsByCurrency(String currency) {
        return transactionRepository.findByCurrencyShortname(currency);
    }

    public Transaction getLastTransaction() {
        return transactionRepository.findTopByOrderByDatetimeDesc();
    }

    public List<Transaction> getTransactionsExceedingLimit() {
        return transactionRepository.findByLimitExceededTrue();
    }
}
