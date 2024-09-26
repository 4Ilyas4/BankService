package com.project.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.models.Limit;
import com.project.demo.repositories.LimitsRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class LimitService {

    @Autowired
    private LimitsRepository limitRepository;

    public void setLimit(Limit limit) {
        LocalDateTime now = LocalDateTime.now();

        if (limit.getLimitDatetime().isBefore(now) || limit.getLimitDatetime().isAfter(now)) {
            throw new IllegalArgumentException("Дата лимита не может быть в прошлом или будущем.");
        }

        Limit lastLimit = limitRepository.findTopByOrderByLimitDatetimeDesc();
        if (lastLimit != null && lastLimit.getLimitDatetime().toLocalDate().equals(now.toLocalDate())) {
            throw new IllegalStateException("Невозможно обновить лимит на ту же дату.");
        }

        limit.setLimitDatetime(now);
        limitRepository.save(limit);
    }


    public List<Limit> getAllLimits() {
        return limitRepository.findAll();
    }

    public Limit getLastLimit() {
        return limitRepository.findTopByOrderByLimitDatetimeDesc();
    }
}