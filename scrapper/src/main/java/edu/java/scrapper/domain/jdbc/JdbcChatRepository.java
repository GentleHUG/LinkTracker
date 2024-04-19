package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.dto.Chat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SuppressWarnings("MultipleStringLiterals")
@Repository
public class JdbcChatRepository {

    private final ChatRowMapper chatRowMapper;
    public JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate, ChatRowMapper chatRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.chatRowMapper = chatRowMapper;
    }

    public List<Chat> findAllChats() {
        String sql = "SELECT * FROM chats";

        return jdbcTemplate.query(sql, chatRowMapper);
    }


    public Chat findChatByTgChatId(Long tgChatId) {
        String sql = "SELECT * FROM chats WHERE chat_id = ?";

        try {
            return jdbcTemplate.query(sql, chatRowMapper, tgChatId).getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Chat add(Long tgChatId) {
        String sql = "INSERT INTO chats VALUES (default, ?, default)";

        jdbcTemplate.update(sql, tgChatId);
        return findChatByTgChatId(tgChatId);
    }

    @Transactional
    public int removeByChatId(Long tgChatId) {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        return jdbcTemplate.update(sql, tgChatId);
    }
}
