package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;

public class ListCommand implements Command{
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

        // Заполнение массива ссылок из БД
        ArrayList<String> linkList = new ArrayList<>();

        StringBuilder messageText = new StringBuilder();

        if (linkList.isEmpty()) {
            messageText.append("Список отслеживаемых ссылок пуст!");
        } else {
            for (int i = 0; i < linkList.size(); i++) {
                messageText.append(i + 1).append(") ").append(linkList.get(i)).append("\n");
            }
        }

        return new SendMessage(update.message().chat().id(), messageText.toString());
    }
}
