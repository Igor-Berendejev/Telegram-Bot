package com.example.tgbot.service;

public enum BotCommands {
    ADD_INCOME ("/addincome"),
    ADD_SPEND ("/addspend"),
    GET_RATE ("/getcurrencyrate"),
    CURRENT_RATES ("/currentrates"),
    SPEND_STATS ("/spendamountstats");

    private String command;

    BotCommands (String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
