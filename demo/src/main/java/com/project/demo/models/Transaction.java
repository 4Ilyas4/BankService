package com.project.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data  
@AllArgsConstructor
@NoArgsConstructor
@Entity 
@Table(name="transactions") 
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String accountFrom;

    @Column(length = 10, nullable = false)
    private String accountTo;

    @Column(length = 3, nullable = false)
    private String currencyShortname;

    @Column(nullable = false)
    private BigDecimal sum;

    @Column(nullable = false)
    private String expenseCategory;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @Column(nullable = false)
    private BigDecimal totalSum;

    @Column(nullable = false)
    private Boolean limitExceeded = false;
}



