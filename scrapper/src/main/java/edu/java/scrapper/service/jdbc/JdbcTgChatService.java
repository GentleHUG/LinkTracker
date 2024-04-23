package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcChatRepositoryNew;
import edu.java.scrapper.exception.ExistChatException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.service.TgChatService;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgChatService implements TgChatService {
    JdbcChatRepositoryNew jdbcChatRepository;

    public JdbcTgChatService(JdbcChatRepositoryNew jdbcChatRepository) {
        this.jdbcChatRepository = jdbcChatRepository;
    }

    @Override
    public void register(long tgChatId) throws ExistChatException {

        try {
            jdbcChatRepository.add(tgChatId);
        } catch (Exception e) {
            throw new ExistChatException();
        }
    }

    @Override
    public void unregister(long tgChatId) throws NotFoundChatException {
        Chat chat;
        try {
            chat = jdbcChatRepository.findChatByTgChatId(tgChatId);
        } catch (Exception e) {
            throw new NotFoundChatException();
        }

        jdbcChatRepository.remove(chat.id());
    }
}
