package com.example.tgbot.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpendRowMapper implements RowMapper<Spend> {
    @Override
    public Spend mapRow(ResultSet rs, int rowNum) throws SQLException {
        Spend spend = new Spend();

        spend.setId(rs.getLong("ID"));
        spend.setChatId(rs.getLong("CHAT_ID"));
        spend.setSpend(rs.getBigDecimal("SPEND"));

        return spend;
    }
}
