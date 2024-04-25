package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.ErrorResponse;
import edu.java.bot.controller.dto.LinkResponse;
import edu.java.bot.controller.dto.RemoveLinkRequest;
import edu.java.bot.util.UrlChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import java.net.URL;

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

        String messageText = "";
        String input = update.message().text();
        Long tgChatId = update.message().from().id();

        int spaceIndex = input.indexOf(' ');
        if (spaceIndex != -1) {
            String url = input.substring(spaceIndex + 1).trim();
            if (UrlChecker.isValid(url)) {

                try {
                    LinkResponse response = scrapperClient.deleteLink(tgChatId,
                        new RemoveLinkRequest(url));
                    messageText = "Отслеживание ссылки успешно завершено: " + response.link();
                } catch (HttpClientErrorException e) {
                    ErrorResponse response = e.getResponseBodyAs(ErrorResponse.class);
                    if (response != null) {
                        messageText = response.code();
                    }
                }
            }
        }


        return new SendMessage(update.message().chat().id(), messageText);
    }
}
