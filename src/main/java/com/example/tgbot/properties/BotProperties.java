package com.example.tgbot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotProperties {

    private String name;

    private BotApiProperties api;

    @Getter
    @Setter
    public class BotApiProperties {
        private String key;
    }
}
