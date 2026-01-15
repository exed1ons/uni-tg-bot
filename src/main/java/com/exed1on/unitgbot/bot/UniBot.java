package com.exed1on.unitgbot.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Component
@Slf4j
public class UniBot extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;

    public UniBot(@Value("${bot.username}") String botName, @Value("${bot.token}") String botToken) {
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/")) {
                handleCommand(messageText, chatId);
            } else {
                sendTextMessage(chatId, "Got your message: " + messageText);
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleCommand(String command, long chatId) {
        switch (command.toLowerCase()) {
            case "/start":
                sendWelcomeMessage(chatId);
                break;
            case "/about":
                sendAboutMessage(chatId);
                break;
            case "/help":
                sendHelpMessage(chatId);
                break;
            case "/facts":
                sendFactsMessage(chatId);
                break;
            case "/menu":
                sendMenuWithButtons(chatId);
                break;
            default:
                sendTextMessage(chatId, "Unknown command. Try /help to see what I can do.");
        }
    }

    private void sendWelcomeMessage(long chatId) {
        String welcomeText = "Hello World!\n" +
                "Welcome to the bot. Type /menu to see what you can do here.";
        sendTextMessage(chatId, welcomeText);
    }

    private void sendAboutMessage(long chatId) {
        String aboutText = "About:\n\n" +
                "Made by Yaroslav\n" +
                "Currently in 7th semester studying computer science\n" +
                "Stack I work with: Java Spring Boot, PostgreSQL, AWS\n\n" +
                "This bot is part of a university assignment.";
        sendTextMessage(chatId, aboutText);
    }

    private void sendHelpMessage(long chatId) {
        String helpText = "Commands:\n\n" +
                "/start - shows welcome message\n" +
                "/about - info about me\n" +
                "/help - this message\n" +
                "/facts - some random tech facts\n" +
                "/menu - interactive menu\n\n" +
                "You can also just send regular messages and I'll echo them back.";
        sendTextMessage(chatId, helpText);
    }

    private void sendFactsMessage(long chatId) {
        String factsText = "Random tech facts:\n\n" +
                "1. The first computer bug was literally a bug - a moth trapped in a relay of the Harvard Mark II in 1947.\n\n" +
                "2. Java was originally called Oak when it was first developed in the early 90s.\n\n" +
                "3. PostgreSQL started as POSTGRES at UC Berkeley back in 1986.\n\n" +
                "4. AWS launched in 2006 with just S3 and EC2. Now look at how many services they have.\n\n" +
                "5. Arctic Monkeys were among the first bands to blow up through MySpace and file sharing instead of traditional record labels.";
        sendTextMessage(chatId, factsText);
    }

    private void sendMenuWithButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Main Menu\n\nPick something:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton aboutButton = InlineKeyboardButton.builder()
                .text("About")
                .callbackData("about")
                .build();
        InlineKeyboardButton helpButton = InlineKeyboardButton.builder()
                .text("Help")
                .callbackData("help")
                .build();
        row1.add(aboutButton);
        row1.add(helpButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton factsButton = InlineKeyboardButton.builder()
                .text("Random Facts")
                .callbackData("facts")
                .build();
        row2.add(factsButton);

        keyboard.add(row1);
        keyboard.add(row2);
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending menu message", e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        switch (callbackData) {
            case "about":
                sendAboutMessage(chatId);
                break;
            case "help":
                sendHelpMessage(chatId);
                break;
            case "facts":
                sendFactsMessage(chatId);
                break;
        }
    }

    private void sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending text message", e);
        }
    }
}