package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.UpdateRequest;
import edu.java.bot.telegram.BotImplementation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UpdateService {
    private final BotImplementation botImplementation;

    public UpdateService(BotImplementation botImplementation) {
        this.botImplementation = botImplementation;
    }

    public void handle(UpdateRequest updateRequest) {
        List<Long> tgCHatIds = updateRequest.tgChatIds();
        String text = String.format(
            "Обновление по ссылке: %s\nОписание: %s",
            updateRequest.url(),
            updateRequest.description());

        for (Long id: tgCHatIds) {
            SendMessage sendMessage = new SendMessage(id, text);
            botImplementation.execute(sendMessage);
        }
    }
}
