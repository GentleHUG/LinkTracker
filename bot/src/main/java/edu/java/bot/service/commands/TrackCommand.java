package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.AddLinkRequest;
import edu.java.bot.controller.dto.ErrorResponse;
import edu.java.bot.controller.dto.LinkResponse;
import edu.java.bot.util.UrlChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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

        Long tgChatId = update.message().from().id();
        String input = update.message().text();
        String messageText = "Введите корректную ссылку";

        String[] split = input.split(" ");

        if (split.length == 2 && UrlChecker.isValid(split[1])) {
            try {
                LinkResponse linkResponse = scrapperClient.createLink(tgChatId, new AddLinkRequest(split[1]));
                messageText = "Ссылка успешно добавлена к отслеживанию: " + linkResponse.link();
            } catch (HttpClientErrorException e) {
                ErrorResponse response = e.getResponseBodyAs(ErrorResponse.class);
                if (response != null) {
                    messageText = response.description();
                }
            }
        }

        return new SendMessage(tgChatId, messageText);
    }
}
