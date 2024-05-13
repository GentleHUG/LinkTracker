package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class StartCommand implements Command {

    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Зарегистрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        String messageText = "";
        Long tgChatId = update.message().from().id();

        try {
            scrapperClient.createTgChat(tgChatId);

            String userId = update.message().from().id().toString();
            String firstName = update.message().from().firstName();
            String lastName = update.message().from().lastName();
            String username = update.message().from().username();

            messageText = String.format("Пользователь %s %s %s (ID: %s) зарегестрирован.",
                username,
                firstName,
                lastName,
                userId
            );

        } catch (HttpClientErrorException e) {
             ErrorResponse response = e.getResponseBodyAs(ErrorResponse.class);
             if (response != null) {
                 messageText = response.code();
             }
        }
        return new SendMessage(tgChatId, messageText);
    }
}
