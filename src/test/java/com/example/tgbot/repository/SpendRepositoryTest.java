package com.example.tgbot.repository;

import com.example.tgbot.entity.Spend;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class SpendRepositoryTest {

    @Autowired
    SpendRepository spendRepository;

    @Test
    public void testAddSpend(){
        Spend spend = spendRepository.findById(666L).get();
        assertNotNull(spend);
        assertEquals(new BigDecimal(678.00).setScale(2), spend.getSpend().setScale(2));
    }

}