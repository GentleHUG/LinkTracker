package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.entity.Chat;
import edu.java.scrapper.exception.ExistChatException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.service.ChatService;
import java.util.Optional;

public class JpaChatService implements ChatService {
    private JpaChatRepository jpaChatRepository;

    public JpaChatService(JpaChatRepository jpaChatRepository) {
        this.jpaChatRepository = jpaChatRepository;
    }

    @Override
    public void register(long tgChatId) throws ExistChatException {
        try {
            Chat chat = new Chat(tgChatId);
            jpaChatRepository.save(chat);
        } catch (Exception e) {
            throw new ExistChatException();
        }
    }

    @Override
    public void unregister(long tgChatId) throws NotFoundChatException {
        Optional<Chat> chat = jpaChatRepository.findChatByChatId(tgChatId);
        if (chat.isPresent()) {
            jpaChatRepository.deleteChatByChatId(tgChatId);
        } else {
            throw new NotFoundChatException();
        }
    }
}
