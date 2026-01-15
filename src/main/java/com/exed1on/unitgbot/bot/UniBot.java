package com.exed1on.unitgbot.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.*;

@Setter
@Getter
@Component
public class TiktokSenderBot extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;

    public TiktokSenderBot(@Value("${bot.username}") String botName, @Value("${bot.token}") String botToken) {

        super(botToken);
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}