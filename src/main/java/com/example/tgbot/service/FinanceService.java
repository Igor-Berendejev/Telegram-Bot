package com.example.tgbot.service;

import com.example.tgbot.entity.Income;
import com.example.tgbot.entity.Spend;
import com.example.tgbot.repository.IncomeRepository;
import com.example.tgbot.repository.SpendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final IncomeRepository incomeRepository;
    private final SpendRepository spendRepository;
    private final StatService statService;

    public String addFinanceOperation(String operationType, String price, Long chatId) {
        String message = "Invalid input";
        if (BotCommands.ADD_INCOME.getCommand().equalsIgnoreCase(operationType)) {
            Income income = new Income();
            income.setChatId(chatId);
            income.setIncome(new BigDecimal(price));
            incomeRepository.save(income);
            message = "Income in amount " + price + " has been successfully added";
        } else if (BotCommands.ADD_SPEND.getCommand().equalsIgnoreCase(operationType)) {
            Spend spend = new Spend();
            spend.setChatId(chatId);
            spend.setSpend(new BigDecimal(price));
            spendRepository.save(spend);
            message = "Spend in amount " + price + " has been successfully added";
        } else if (BotCommands.SPEND_STATS.getCommand().equalsIgnoreCase(operationType)) {
            message = "List of spends greater than " + price + ": " + statService.getSpendsGreaterThen(new BigDecimal(price));
        }
        return message;
    }
}
