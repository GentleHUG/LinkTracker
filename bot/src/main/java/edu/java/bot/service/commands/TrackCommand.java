package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.util.UrlChecker;

public class TrackCommand implements Command {
    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {

        String messageText = "Введите корректную ссылку";
        String input = update.message().text();

        int spaceIndex = input.indexOf(' ');
        if (spaceIndex != -1) {
            String url = input.substring(spaceIndex + 1).trim();
            if (UrlChecker.isValid(url)) {

                // НУЖНО ДОБАВИТЬ ФУНКЦИОНАЛ
                messageText = "Ссылка успешно добавлена к отслеживанию: " + url;
            }
        }


        return new SendMessage(update.message().chat().id(), messageText);
    }
}
