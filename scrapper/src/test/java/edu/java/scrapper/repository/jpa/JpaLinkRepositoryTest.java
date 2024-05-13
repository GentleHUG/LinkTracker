package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.Chat;
import edu.java.scrapper.domain.jpa.entity.Link;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JpaLinkRepositoryTest extends IntegrationTest {
    private final String URL = "https://github.com/GentleHUG/LinkTracker";

    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat chat = new Chat(10L);
        jpaChatRepository.save(chat);
        Link link = new Link(URL, chat);

        Link addLink = jpaLinkRepository.save(link);
        List<Link> links = jpaLinkRepository.findAll();

        Assertions.assertEquals(1, links.size());
        Assertions.assertEquals(URL, links.getFirst().getUrl());
        Assertions.assertEquals(URL, addLink.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        Chat chat = jpaChatRepository.save(new Chat(10L));
        Link link = jpaLinkRepository.save(new Link(URL, chat));

        link.deleteChat(chat);
        if (link.getChats().isEmpty()) {
            jpaLinkRepository.delete(link);
        }

        List<Link> links = jpaLinkRepository.findAll();
        Assertions.assertEquals(0, chat.getLinks().size());
        Assertions.assertEquals(0, links.size());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Chat chat1 = jpaChatRepository.save(new Chat(10L));
        Chat chat2 = jpaChatRepository.save(new Chat(11L));

        Link link1 = jpaLinkRepository.save(new Link("https://java.com", chat1));
        Link link2 = jpaLinkRepository.save(new Link("https://google.com", chat1));
        Link link3 = jpaLinkRepository.save(new Link("https://github.com", chat2));

        List<Link> links = jpaLinkRepository.findAll();
        Assertions.assertEquals(3, links.size());

        Assertions.assertEquals(link1.getUrl(), links.getFirst().getUrl());
        Assertions.assertEquals(link3.getUrl(), links.getLast().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void findLinkByUrlTest() {
        Chat chat = jpaChatRepository.save(new Chat(10L));
        Link link = jpaLinkRepository.save(new Link(URL, chat));

        Optional<Link> linkOptional = jpaLinkRepository.findLinkByUrl(URL);
        Assertions.assertTrue(linkOptional.isPresent());
        Assertions.assertEquals(URL, linkOptional.get().getUrl());
        Assertions.assertEquals(chat, linkOptional.get().getChats().getFirst());
    }
}
