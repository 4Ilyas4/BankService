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
@Table(name="limits") 
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal limitSum;

    @Column(nullable = false)
    private LocalDateTime limitDatetime;

    @Column(length = 3, nullable = false)
    private String limitCurrencyShortname;
}

