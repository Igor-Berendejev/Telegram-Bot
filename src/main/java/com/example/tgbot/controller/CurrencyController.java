package com.example.tgbot.controller;

import com.example.tgbot.DTO.ValuteCursOnDate;
import com.example.tgbot.entity.Spend;
import com.example.tgbot.service.CentralRussianBankService;
import com.example.tgbot.service.StatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CentralRussianBankService centralRussianBankService;
    private final StatService statService;

    @GetMapping("/getCurrencies")
    public List<ValuteCursOnDate> getValuteCursOnDate() throws Exception {
        return centralRussianBankService.getCurrenciesFromCbr();
    }

    @GetMapping("/getIncomeStats")
    @ApiOperation(value = "Get number of incomes that are greater then certain amount")
    public int getStatsForIncomesGreaterThen(@RequestParam(value = "amount") BigDecimal amount) {
        return statService.getCountOfIncomesGreaterThen(amount);
    }

    @GetMapping("/getSpendStats")
    @ApiOperation(value = "Get the list of spends greater then certain amount")
    public List<Spend> getSpendsGreaterThen(@RequestParam(value = "amount") BigDecimal amount) {
        return statService.getSpendsGreaterThen(amount);
    }
}
