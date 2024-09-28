package com.project.demo.services;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void initializeLimitOnStartup() {
        if(getLastLimit() == null) {
            Limit defaultLimit = new Limit();
            defaultLimit.setLimitDatetime(LocalDateTime.now());
            limitRepository.save(defaultLimit);
        }
    }

    public void setLimit(Limit limit) {
        if (limit == null) {
            throw new IllegalArgumentException("Лимит не может быть null");
        }
        LocalDateTime now = LocalDateTime.now();
        limit.setLimitDatetime(now);
        Limit lastLimit = limitRepository.findTopByOrderByLimitDatetimeDesc();
        if (lastLimit != null && lastLimit.getLimitDatetime().toLocalDate().equals(now.toLocalDate())) {
            throw new IllegalStateException("Невозможно обновить лимит на ту же дату.");
        }
        limitRepository.save(limit);
    }

    public List<Limit> getAllLimits() {
        return limitRepository.findAll();
    }

    public Limit getLastLimit() {
        return limitRepository.findTopByOrderByLimitDatetimeDesc();
    }
}