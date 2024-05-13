package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jdbc.JdbcChatRepositoryNew;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepositoryNew;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.exception.ExistLinkException;
import edu.java.scrapper.exception.NotFoundLinkException;
import edu.java.scrapper.service.LinkService;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;


public class JdbcLinkService implements LinkService {
    JdbcLinkRepositoryNew jdbcLinkRepository;
    JdbcChatRepositoryNew jdbcChatRepository;

    public JdbcLinkService(JdbcLinkRepositoryNew jdbcLinkRepository, JdbcChatRepositoryNew jdbcChatRepository) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcChatRepository = jdbcChatRepository;
    }

    @Transactional
    @Override
    public Link add(long tgChatId, URI url) throws NotFoundChatException, ExistLinkException {
        Chat chat;
        try {
            chat = jdbcChatRepository.findChatByTgChatId(tgChatId);
        } catch (Exception e) {
            throw new NotFoundChatException();
        }

        Link link;
        try {
            link = jdbcLinkRepository.findLinkByUrl(url.toString());
        } catch (Exception e) {
            jdbcLinkRepository.add(url.toString());
            link = jdbcLinkRepository.findLinkByUrl(url.toString());

        }

        try {
            jdbcLinkRepository.addLinkIdToChatId(link.id(), chat.id());
        } catch (Exception e) {
            throw new ExistLinkException();
        }
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) throws NotFoundChatException, NotFoundLinkException {
        Chat chat;

        try {
            chat = jdbcChatRepository.findChatByTgChatId(tgChatId);
        } catch (Exception e) {
            throw new NotFoundChatException();
        }

        Link link;
        try {
            link = jdbcLinkRepository.findLinkByUrl(url.toString());
        } catch (Exception e) {
            throw new NotFoundLinkException();
        }

        if (!jdbcLinkRepository.isLinkConnectedToChat(chat.id(), link.id())) {
            throw new NotFoundLinkException();
        }

        jdbcLinkRepository.removeLinkToChat(chat.id(), link.id());
        if (!jdbcLinkRepository.isLinkConnected(link.id())) {
            jdbcLinkRepository.removeLink(link.id());
        }

        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) throws NotFoundChatException {
        try {
            Chat chat = jdbcChatRepository.findChatByTgChatId(tgChatId);
            return jdbcLinkRepository.findAllByChatId(chat.id());
        } catch (Exception e) {
            throw new NotFoundChatException();
        }
    }
}
