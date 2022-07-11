package com.example.tgbot.service;

import com.example.tgbot.entity.Spend;
import com.example.tgbot.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatsRepository statsRepository;

    public int getCountOfIncomesGreaterThen(BigDecimal amount) {
        return statsRepository.getCountOfIncomesGreaterThen(amount);
    }

    public List<Spend> getSpendsGreaterThen(BigDecimal amount) {
        return statsRepository.getSpendsGreaterThen(amount);
    }
}
