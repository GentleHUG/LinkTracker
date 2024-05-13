package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.Chat;
import edu.java.scrapper.exception.ExistLinkException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.exception.NotFoundLinkException;
import edu.java.scrapper.service.LinkService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public class JpaLinkService implements LinkService {

    JpaChatRepository jpaChatRepository;
    JpaLinkRepository jpaLinkRepository;

    public JpaLinkService(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        this.jpaChatRepository = jpaChatRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    @Transactional
    public Link add(long tgChatId, URI url) throws NotFoundChatException, ExistLinkException {
        Optional<Chat> optionalChat = jpaChatRepository.findChatByChatId(tgChatId);

        if (optionalChat.isEmpty()) {
            throw new NotFoundChatException();
        }

        Chat chat = optionalChat.get();
        Optional<edu.java.scrapper.domain.jpa.entity.Link> optionalLink = jpaLinkRepository.findLinkByUrl(url.toString());

        edu.java.scrapper.domain.jpa.entity.Link link;

        if (optionalLink.isEmpty()) {
            link = new edu.java.scrapper.domain.jpa.entity.Link(url.toString(), chat);
        } else {

            link = optionalLink.get();

            if (link.getChats().contains(chat) || chat.getLinks().contains(link)) {
                throw new ExistLinkException();
            }

            link.addChat(chat);
        }

        try {
            link = jpaLinkRepository.save(link);
            return Link.createFromJpa(link);
        } catch (DuplicateKeyException e) {
            throw new ExistLinkException();
        }
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) throws NotFoundChatException, NotFoundLinkException {
        Optional<Chat> optionalChat = jpaChatRepository.findChatByChatId(tgChatId);
        if (optionalChat.isEmpty()) {
            throw new NotFoundChatException();
        }

        Optional<edu.java.scrapper.domain.jpa.entity.Link> optionalLink = jpaLinkRepository.findLinkByUrl(url.toString());
        if (optionalLink.isEmpty()) {
            throw new NotFoundLinkException();
        }

        Chat chat = optionalChat.get();
        edu.java.scrapper.domain.jpa.entity.Link link = optionalLink.get();

        if (!chat.getLinks().contains(link)) {
            throw new NotFoundLinkException();
        }

        link.deleteChat(chat);
        if (link.getChats().isEmpty()) {
            jpaLinkRepository.delete(link);
        } else {
            jpaLinkRepository.save(link);
        }

        return Link.createFromJpa(link);

    }

    @Override
    public Collection<Link> listAll(long tgChatId) throws NotFoundChatException {
        Optional<Chat> optionalChat = jpaChatRepository.findChatByChatId(tgChatId);

        if (optionalChat.isEmpty()) {
            throw new NotFoundChatException();
        }

        Chat chat = optionalChat.get();
        return chat.getLinks().stream()
            .map(Link::createFromJpa)
            .toList();
    }
}
