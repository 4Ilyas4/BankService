package com.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.demo.models.Limit;
import org.springframework.data.jpa.repository.Query;

public interface LimitsRepository extends JpaRepository<Limit,Long> {
    @Query("SELECT l FROM Limit l ORDER BY l.limitDatetime DESC")
    Limit findTopByOrderByLimitDatetimeDesc();
}