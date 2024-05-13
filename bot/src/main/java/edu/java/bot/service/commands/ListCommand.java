package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.ErrorResponse;
import edu.java.bot.controller.dto.ListLinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListCommand implements Command{
    private final String EMPTY_LIST = "Список отлеживаемых ссылок пуст!";


    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {

        Long tgChatId = update.message().chat().id();
        StringBuilder messageText = new StringBuilder();


        try {
            ListLinkResponse response = scrapperClient.getLinks(tgChatId);

            if (response.size() == 0) {
                messageText.append(EMPTY_LIST);
            } else {
                for (int i = 0; i < response.size(); i++) {
                    messageText.append(i + 1).append(") ").append(response.links().get(i).link()).append("\n");
                }
            }
        } catch (HttpClientErrorException e) {
            ErrorResponse response = e.getResponseBodyAs(ErrorResponse.class);
            if (response != null) {
                messageText.append(response.description());
            }
        }

        return new SendMessage(tgChatId, messageText.toString());
    }
}
