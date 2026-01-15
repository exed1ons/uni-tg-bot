package com.exed1on.unitgbot.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Getter
@Component
@Slf4j
public class BotInitializer {

    private final UniBot uniBot;

    public BotInitializer(UniBot uniBot) {
        this.uniBot = uniBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(uniBot);
        } catch (Exception e) {
            log.error("Error while initializing bot", e);
        }
    }
}
