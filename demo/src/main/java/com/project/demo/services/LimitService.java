package com.project.demo.services;

import lombok.AllArgsConstructor;
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

        if (now.getDayOfMonth() != 1) {
            throw new IllegalArgumentException("Обновление лимита разрешено только в первое число месяца.");
        }

        limit.setLimitDatetime(now);
        Limit lastLimit = limitRepository.findTopByOrderByLimitDatetimeDesc();
        if (lastLimit != null && lastLimit.getLimitDatetime().toLocalDate().equals(now.toLocalDate())) {
            throw new IllegalStateException("Невозможно обновить лимит на ту же дату.");
        }
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
            lastLimit = new Limit();
            lastLimit.setLimitDatetime(LocalDateTime.now());
            setLimit(lastLimit);
        }
        return lastLimit;
    }
}