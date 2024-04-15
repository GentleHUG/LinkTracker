package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.exception.ExistsLinkException;
import edu.java.scrapper.exception.NotFoundLinkException;
import edu.java.scrapper.service.LinkService;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.Collection;

@Service
public class JdbcLinkService implements LinkService {
    JdbcLinkRepository jdbcLinkRepository;
    JdbcChatRepository jdbcChatRepository;

    public JdbcLinkService(JdbcLinkRepository jdbcLinkRepository, JdbcChatRepository jdbcChatRepository) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcChatRepository = jdbcChatRepository;
    }

    @Override
    public Link add(long tgChatId, URI url) throws NotFoundChatException, ExistsLinkException {
        Chat chat = jdbcChatRepository.findChatByTgChatId(tgChatId);

        if (chat == null) {
            throw new NotFoundChatException();
        }

        try {
            return jdbcLinkRepository.add(chat.chatId(), url.toString());
        } catch (Exception e) {
            throw new ExistsLinkException();
        }
    }

    @Override
    public Link remove(long tgChatId, URI url) throws NotFoundChatException, NotFoundLinkException {
        Chat chat = jdbcChatRepository.findChatByTgChatId(tgChatId);

        if (chat == null) {
            throw new NotFoundChatException();
        }

        Link link = jdbcLinkRepository.remove(chat.chatId(), url.toString());

        if (link == null) {
            throw new NotFoundLinkException();
        }
        return link;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) throws NotFoundChatException {
        Chat chat = jdbcChatRepository.findChatByTgChatId(tgChatId);
        if (chat == null) {
            throw new NotFoundChatException();
        }
        return jdbcLinkRepository.findAllByChatId(chat.chatId());
    }
}
