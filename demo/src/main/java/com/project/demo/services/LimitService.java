package com.project.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.models.Limit;
import com.project.demo.repositories.LimitsRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class LimitService {

    private LimitsRepository limitRepository;

    public void setLimit(Limit limit) {
        if (limit == null) {
            throw new IllegalArgumentException("Лимит не может быть null");
        }

        LocalDateTime now = LocalDateTime.now();

        if (limit.getLimitDatetime() == null || limit.getLimitDatetime().isBefore(now) || limit.getLimitDatetime().isAfter(now)) {
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
        List<Limit> limits = limitRepository.findAll();
        if (limits.isEmpty()) {
            throw new IllegalStateException("Лимиты не найдены.");
        }
        return limits;
    }

    public Limit getLastLimit() {
        Limit lastLimit = limitRepository.findTopByOrderByLimitDatetimeDesc();
        if (lastLimit == null) {
            throw new IllegalStateException("Последний лимит не найден.");
        }
        return lastLimit;
    }
}