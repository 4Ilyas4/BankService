package com.project.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.demo.models.Transaction;
import org.springframework.data.jpa.repository.Query;

public interface TransactionsRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByExpenseCategory(String expenseCategory);
    List<Transaction> findByCurrencyShortname(String currencyShortname);
    @Query("SELECT t FROM Transaction t ORDER BY t.datetime DESC")
    Transaction findTopByOrderByDatetimeDesc();
    List<Transaction> findByLimitExceededTrue();
}
