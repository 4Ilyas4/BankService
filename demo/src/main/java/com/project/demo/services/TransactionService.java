package com.project.demo.services;

import com.project.demo.models.Limit;
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
        if (transaction == null || transaction.getSum() == null) {
            throw new IllegalArgumentException("Транзакция и её сумма не могут быть null");
        }

        Transaction lastTransaction = getLastTransaction();
        if (lastTransaction == null || lastTransaction.getTotalSum() == null) {
            throw new IllegalStateException("Последняя транзакция или её сумма не найдены.");
        }
        BigDecimal totalSum = lastTransaction.getTotalSum().add(transaction.getSum());
        transaction.setTotalSum(totalSum);

        Limit lastLimit = limitService.getLastLimit();
        if (lastLimit == null || lastLimit.getLimitSum() == null) {
            throw new IllegalStateException("Последний лимит не найден.");
        }

        BigDecimal limit = lastLimit.getLimitSum();
        if (totalSum.compareTo(limit) > 0) {
            transaction.setLimitExceeded(true);
        }
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Категория не может быть null или пустой");
        }
        List<Transaction> transactions = transactionRepository.findByExpenseCategory(category);
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalStateException("Транзакции для категории '" + category + "' не найдены.");
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByCurrency(String currency) {
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Валюта не может быть null или пустой");
        }
        List<Transaction> transactions = transactionRepository.findByCurrencyShortname(currency);
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalStateException("Транзакции для валюты '" + currency + "' не найдены.");
        }
        return transactions;
    }

    public Transaction getLastTransaction() {
        Transaction lastTransaction = transactionRepository.findTopByOrderByDatetimeDesc();
        if (lastTransaction == null) {
            throw new IllegalStateException("Последняя транзакция не найдена.");
        }
        return lastTransaction;
    }

    public List<Transaction> getTransactionsExceedingLimit() {
        List<Transaction> transactions = transactionRepository.findByLimitExceededTrue();
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalStateException("Нет транзакций, превысивших лимит.");
        }
        return transactions;
    }
}
