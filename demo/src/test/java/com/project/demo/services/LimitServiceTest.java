package com.project.demo.services;

import com.project.demo.models.Limit;
import com.project.demo.repositories.LimitsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LimitServiceTest {

    @InjectMocks
    private LimitService limitService;

    @Mock
    private LimitsRepository limitsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void setLimit() {
        Limit limit = new Limit();
        limitService.setLimit(limit);
        verify(limitsRepository, times(1)).save(limit);
    }

    @Test
    void testGetAllLimits() {
        Limit limit1 = new Limit();
        Limit limit2 = new Limit();
        List<Limit> mockLimits = Arrays.asList(limit1, limit2);
        when(limitsRepository.findAll()).thenReturn(mockLimits);
        List<Limit> result = limitService.getAllLimits();
        assertThat(result).hasSize(2);
        assertThat(result).contains(limit1, limit2);
    }

    @Test
    void testGetLastLimit() {
        Limit lastLimit = new Limit();
        lastLimit.setLimitDatetime(LocalDateTime.now());
        when(limitsRepository.findTopByOrderByLimitDatetimeDesc()).thenReturn(lastLimit);
        Limit result = limitService.getLastLimit();
        assertThat(result).isNotNull();
        assertThat(result.getLimitDatetime()).isEqualTo(lastLimit.getLimitDatetime());
    }
}
