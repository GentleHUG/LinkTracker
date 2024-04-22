package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.AddLinkRequest;
import edu.java.bot.util.UrlChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {

    @Autowired
    private ScrapperClient scrapperClient;

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

        String[] split = input.split(" ");

        if (split.length == 2 && UrlChecker.isValid(split[1])) {
            try {
                scrapperClient.createLink(update.message().from().id(), new AddLinkRequest(split[1]));
                messageText = "Ссылка успешно добавлена: \n" + split[1];
            } catch (Exception e) {
                messageText = e.getMessage();
            }
        }

        return new SendMessage(update.message().chat().id(), messageText);
    }
}
