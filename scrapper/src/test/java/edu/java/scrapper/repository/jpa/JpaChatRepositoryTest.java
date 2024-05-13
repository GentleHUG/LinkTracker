package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.entity.Chat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JpaChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat addedChat = jpaChatRepository.save(new Chat(1L));
        List<Chat> chats = jpaChatRepository.findAll();

        Assertions.assertEquals(1, chats.size());
        Assertions.assertEquals(1L, addedChat.getChatId());
        Assertions.assertEquals(1L, chats.getFirst().getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jpaChatRepository.save(new Chat(5L));
        jpaChatRepository.save(new Chat(6L));

        List<Chat> chats = jpaChatRepository.findAll();
        Assertions.assertEquals(2, chats.size());
        Assertions.assertEquals(5L, chats.getFirst().getChatId());
        Assertions.assertEquals(6L, chats.getLast().getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void findChatByChatIdTest() {
        Chat chat = new Chat(8L);
        jpaChatRepository.save(chat);

        Optional<Chat> optionalChat1 = jpaChatRepository.findChatByChatId(8L);
        Optional<Chat> optionalChat2 = jpaChatRepository.findChatByChatId(1L);

        Assertions.assertTrue(optionalChat1.isPresent());
        Assertions.assertEquals(8L, optionalChat1.get().getChatId());
        Assertions.assertTrue(optionalChat2.isEmpty());

    }

    @Test
    @Transactional
    @Rollback
    void deleteChatByChatIdTest() {
        jpaChatRepository.save(new Chat(2L));

        Assertions.assertEquals(1, jpaChatRepository.findAll().size());
        jpaChatRepository.deleteChatByChatId(2L);

        Assertions.assertEquals(0, jpaChatRepository.findAll().size());
    }


}
