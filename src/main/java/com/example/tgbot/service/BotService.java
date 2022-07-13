package com.example.tgbot.service;

import com.example.tgbot.DTO.ValuteCursOnDate;
import com.example.tgbot.entity.ActiveChat;
import com.example.tgbot.repository.ActiveChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

    private final CentralRussianBankService centralRussianBankService;
    private final ActiveChatRepository activeChatRepository;
    private final FinanceService financeService;

    private Map<Long, List<String>> previousCommands = new ConcurrentHashMap<>();


    @Value("${bot.api.key}")
    private String apiKey;

    @Value("${bot.name}")
    private String name;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return apiKey;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        try {
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(String.valueOf(chatId));

            if (BotCommands.CURRENT_RATES.getCommand().equalsIgnoreCase(message.getText())) {
                for (ValuteCursOnDate valuteCursOnDate : centralRussianBankService.getCurrenciesFromCbr()) {
                    response.setText(StringUtils.defaultIfBlank(response.getText(), "") +
                            valuteCursOnDate.getName() + "-" +
                            valuteCursOnDate.getCourse() + "\n");
                }
            } else if (BotCommands.ADD_INCOME.getCommand().equalsIgnoreCase(message.getText())) {
                response.setText("Send me an amount of received income");
            } else if (BotCommands.ADD_SPEND.getCommand().equalsIgnoreCase(message.getText())) {
                response.setText("Send me an amount of spend received");
            } else if (BotCommands.SPEND_STATS.getCommand().equalsIgnoreCase(message.getText())) {
                response.setText("Send me an amount of spend you need statistic for");
            } else if (BotCommands.GET_RATE.getCommand().equalsIgnoreCase(message.getText())) {
                response.setText("Send me a currency code you want to check");
            } else {
                if (getPreviousCommand(message.getChatId()).equalsIgnoreCase(BotCommands.GET_RATE.getCommand())) {
                    response.setText(message.getText() +
                            " rate for now: " +
                            centralRussianBankService.getCurrencyRate(message.getText()).getCourse());
                } else {
                    response.setText(financeService.addFinanceOperation(getPreviousCommand(message.getChatId()), message.getText(), message.getChatId()));
                }
            }

            putPreviousCommand(message.getChatId(), message.getText());
            execute(response);

            if (activeChatRepository.findActiveChatByChatId(chatId).isEmpty()) {
                ActiveChat activeChat = new ActiveChat();
                activeChat.setChatId(chatId);
                activeChatRepository.save(activeChat);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void start() {
        log.info("username: {}, token: {}", name, apiKey);
    }

    public void sendNotificationToAllActiveChats(String message, Set<Long> chatIds) {
        for (Long id : chatIds) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(id));
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void putPreviousCommand(Long chatId, String command) {
        if (previousCommands.get(chatId) == null) {
            List<String> commands = new ArrayList<>();
            commands.add(command);
            previousCommands.put(chatId, commands);
        } else {
            previousCommands.get(chatId).add(command);
        }
    }

    private String getPreviousCommand(Long chatId) {
        return previousCommands.get(chatId).get(previousCommands.get(chatId).size() - 1);
    }
}
