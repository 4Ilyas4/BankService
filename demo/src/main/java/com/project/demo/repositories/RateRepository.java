package com.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.demo.models.Rate;

public interface RateRepository extends JpaRepository<Rate,Long> {
    Rate findBySymbol(String symbol);
}
