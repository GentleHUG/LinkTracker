package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListCommand implements Command{

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

        StringBuilder messageText = new StringBuilder();


        try {
            Long tgChatId = update.message().from().id();
            var listLinks = scrapperClient.getLinks(tgChatId);
            if (listLinks.size() == 0) {
                messageText.append("Список отслеживаемых ссылок пуст!");
            } else {
                messageText.append("Вот список ссылок!\n");
                for (int i = 0; i < listLinks.size(); i++) {
                    messageText.append(i + 1).append(listLinks.links().get(i).link()).append("\n");
                }
            }

        } catch (Exception e) {
            messageText.append(e.getMessage());
        }

        return new SendMessage(update.message().chat().id(), messageText.toString());
    }
}
