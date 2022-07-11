package com.example.tgbot.service;

import com.example.tgbot.DTO.ValuteCursOnDate;
import com.example.tgbot.entity.ActiveChat;
import com.example.tgbot.repository.ActiveChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchedulerService {

    private final ActiveChatRepository activeChatRepository;
    private final BotService botService;
    private final CentralRussianBankService centralRussianBankService;
    private final List<ValuteCursOnDate> previousRates = new ArrayList<>();

    @Scheduled(cron = "0 0 0/3 ? * *")
    public void notifyAboutChangesInCurrencyRate(){
        try{
            List<ValuteCursOnDate> currentRates = centralRussianBankService.getCurrenciesFromCbr();
            Set<Long> activeChatIds = activeChatRepository.findAll().stream().map(ActiveChat::getChatId).collect(Collectors.toSet());
            if (!previousRates.isEmpty()){
                for (int i = 0; i < currentRates.size(); i++){
                    if(currentRates.get(i).getCourse() - previousRates.get(i).getCourse() >= 10.0) {
                        botService.sendNotificationToAllActiveChats("Exchange rate " +
                                currentRates.get(i).getName() + " grew over 10 Rubles", activeChatIds);
                    } else if (currentRates.get(i).getCourse() - previousRates.get(i).getCourse() <= 10.0) {
                        botService.sendNotificationToAllActiveChats("Exchange rate " +
                                currentRates.get(i).getName() + " dropped over 10 Rubles", activeChatIds);
                    }
                }
            } else {
                previousRates.addAll(currentRates);
            }
        } catch (DatatypeConfigurationException e){
            e.printStackTrace();
        }
    }
}
