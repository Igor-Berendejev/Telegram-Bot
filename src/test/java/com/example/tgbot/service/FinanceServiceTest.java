package com.example.tgbot.service;

import com.example.tgbot.repository.IncomeRepository;
import com.example.tgbot.repository.SpendRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceServiceTest {

    @Mock
    IncomeRepository incomeRepository;
    @Mock
    SpendRepository spendRepository;
    @Mock
    StatService statService;

    @InjectMocks
    FinanceService financeService;
    
    @Test
    public void testAddFinanceOperation(){
        String price = "150.8";
        String message = financeService.addFinanceOperation("/addincome", price, 500L);
        assertEquals("Income in amount " + price + " has been successfully added", message);

        price = "200.7";
        message = financeService.addFinanceOperation("/addspend", price, 500L);
        assertEquals("Spend in amount " + price + " has been successfully added", message);
    }

}