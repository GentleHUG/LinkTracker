package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.exception.ExistChatException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.service.TgChatService;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgChatService implements TgChatService {
    JdbcChatRepository jdbcChatRepository;

    public JdbcTgChatService(JdbcChatRepository jdbcChatRepository) {
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
        int result = jdbcChatRepository.removeByChatId(tgChatId);
        if (result == 0) {
            throw new NotFoundChatException();
        }
    }
}
