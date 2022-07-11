package com.example.tgbot.repository;

import com.example.tgbot.entity.Spend;
import com.example.tgbot.entity.SpendRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatsRepository {

    private final JdbcTemplate jdbcTemplate;

    public int getCountOfIncomesGreaterThen(BigDecimal amount) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM INCOMES where INCOME > ?", Integer.class, amount);
    }

    public List<Spend> getSpendsGreaterThen(BigDecimal amount) {
        return jdbcTemplate.query("SELECT * FROM SPEND where SPEND > ?", new SpendRowMapper(), amount);
    }
}
