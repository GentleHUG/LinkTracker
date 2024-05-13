package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Long addedChat = jdbcChatRepository.add(7L).chatId();
        List<Chat> chats = jdbcChatRepository.findAllChats();

        Assertions.assertEquals(7L, addedChat);
        Assertions.assertEquals(1, chats.size());
        Assertions.assertEquals(7L, chats.getFirst().chatId());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jdbcChatRepository.add(1L);
        jdbcChatRepository.add(2L);

        List<Chat> chats = jdbcChatRepository.findAllChats();
        Assertions.assertEquals(2, chats.size());
        Assertions.assertEquals(1L, chats.getFirst().chatId());
        Assertions.assertEquals(2L, chats.getLast().chatId());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcChatRepository.add(1L);

        int res1 = jdbcChatRepository.removeByChatId(1L);
        int res2 = jdbcChatRepository.removeByChatId(2L);

        Assertions.assertEquals(1, res1);
        Assertions.assertEquals(0, res2);

        List<Chat> chats = jdbcChatRepository.findAllChats();
        Assertions.assertEquals(0, chats.size());
    }

    @Test
    @Transactional
    @Rollback
    void findChatIDByTgChatIdTest() {
        jdbcChatRepository.add(1L);

        Chat chatId1 = jdbcChatRepository.findChatByTgChatId(1L);
        Chat chatId2 = jdbcChatRepository.findChatByTgChatId(2L);

        Assertions.assertNotNull(chatId1);
        Assertions.assertNull(chatId2);
    }
}
