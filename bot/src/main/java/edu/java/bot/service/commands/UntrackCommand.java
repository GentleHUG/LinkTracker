package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.AddLinkRequest;
import edu.java.bot.controller.dto.ErrorResponse;
import edu.java.bot.controller.dto.LinkResponse;
import edu.java.bot.controller.dto.RemoveLinkRequest;
import edu.java.bot.util.UrlChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class UntrackCommand implements Command {

    @Autowired
    ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update){

        Long tgChatId = update.message().from().id();
        String input = update.message().text();
        String messageText = "Введите корректную ссылку";

        String[] split = input.split(" ");

        if (split.length == 2 && UrlChecker.isValid(split[1])) {
            try {
                LinkResponse linkResponse = scrapperClient.deleteLink(tgChatId, new RemoveLinkRequest(split[1]));
                messageText = "Ссылка успешно удалена из отлеживания: " + linkResponse.link();
            } catch (HttpClientErrorException e) {
                ErrorResponse response = e.getResponseBodyAs(ErrorResponse.class);
                if (response != null) {
                    messageText = response.description();
                }
            }
        }

        return new SendMessage(update.message().chat().id(), messageText);
    }
}
