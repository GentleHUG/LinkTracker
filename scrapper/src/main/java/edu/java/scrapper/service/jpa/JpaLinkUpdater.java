package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.Chat;
import edu.java.scrapper.service.LinkUpdater;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class JpaLinkUpdater implements LinkUpdater {

    private final JpaLinkRepository jpaLinkRepository;

    public JpaLinkUpdater(JpaLinkRepository jpaLinkRepository) {
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    @Transactional
    public int update(Long linkId) {
        Optional<edu.java.scrapper.domain.jpa.entity.Link> optionalLink = jpaLinkRepository.findById(linkId);
        if (optionalLink.isEmpty()) {
            return 0;
        }

        edu.java.scrapper.domain.jpa.entity.Link link = optionalLink.get();
        link.setLastCheckTime(OffsetDateTime.now());
        jpaLinkRepository.save(link);
        return 1;
    }

    @Override
    public Collection<Link> listAllOldCheckedLinks(Long forceCheckDelay) {
        return jpaLinkRepository.findOldCheckedLinks(forceCheckDelay).stream()
            .map(Link::createFromJpa)
            .toList();
    }

    @Override
    public List<Long> listAllTgChatIdByLinkId(Long linkId) {
        Optional<edu.java.scrapper.domain.jpa.entity.Link> optionalLink = jpaLinkRepository.findById(linkId);
        if (optionalLink.isEmpty()) {
            return new ArrayList<>(0);
        }

        edu.java.scrapper.domain.jpa.entity.Link link = optionalLink.get();
        return link.getChats().stream()
            .map(Chat::getChatId)
            .toList();
    }
}
