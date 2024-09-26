package com.project.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.models.Rate;
import com.project.demo.repositories.RateRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

@AllArgsConstructor
@Transactional
@Service
public class RateService {

    private static final String API_KEY = "6d241da463a94566ae7d57dba270f501";
    private static final String BASE_URL = "https://api.twelvedata.com/currency_conversion?symbol=";
    private RateRepository repository;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeRatesOnStartup() {
        saveRates();  
    }

    public CompletableFuture<String> getCurrencyRate(String symbol) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        return client.prepareGet(BASE_URL + symbol + "&amount=122&apikey=" + API_KEY)
            .execute()
            .toCompletableFuture()
            .thenApply(Response::getResponseBody)
            .whenComplete((response, throwable) -> {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }); 
    }

    public Rate getKztUsdRate() {
        return repository.findBySymbol("KZT/USD");
    }
    public Rate getRubUsdRate() {
        return repository.findBySymbol("RUB/USD");
    }

    public CompletableFuture<Rate> getKztToUsdRate() {
        return getCurrencyRate("KZT/USD")
            .thenApply(response -> {
                try {
                    return objectMapper.readValue(response, Rate.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Ошибка при десериализации ответа API", e);
                }
            });
    }

    public CompletableFuture<Rate> getRubToUsdRate() {
        return getCurrencyRate("RUB/USD")
            .thenApply(response -> {
                try {
                    return objectMapper.readValue(response, Rate.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Ошибка при десериализации ответа API", e);
                }
            });
    }

    @Scheduled(cron = "0 0 0 * * ?") 
    public void saveRates() {
        LocalDate today = LocalDate.now();
        getKztToUsdRate().thenAccept(rate -> {
            Rate existingRate = repository.findBySymbol("KZT/USD");

            if (rate != null && rate.getRate() > 0) {
                rate.setLastUpdated(today);
                repository.save(rate);  
            } else if (existingRate != null) {
                existingRate.setLastUpdated(today);
                repository.save(existingRate);  
            }
        });
        getRubToUsdRate().thenAccept(rate -> {
            Rate existingRate = repository.findBySymbol("RUB/USD");

            if (rate != null && rate.getRate() > 0) {
                rate.setLastUpdated(today);
                repository.save(rate); 
            } else if (existingRate != null) {
                existingRate.setLastUpdated(today);
                repository.save(existingRate);
            }
        });
    }

    public Rate findRateBySymbol(String symbol) {
        return repository.findBySymbol(symbol);
    }
}   
