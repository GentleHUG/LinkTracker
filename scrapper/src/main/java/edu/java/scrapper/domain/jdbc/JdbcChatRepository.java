package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.utils.DateParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class JdbcChatRepository {

    public JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Chat> findAllChats() {
        String sql = "SELECT * FROM chats";

        return jdbcTemplate.query(sql, (row, item) ->
            new Chat(
                row.getLong("id"),
                row.getLong("chat_id"),
                DateParser.parseDate(row.getString("addition_time"))
            ));
    }


    public Chat findChatByTgChatId(Long tgChatId) {
        String sql = "SELECT * FROM chats WHERE chat_id = ?";

        try {
            return jdbcTemplate.query(sql, (row, item) ->
                new Chat(
                    row.getLong("id"),
                    row.getLong("chat_id"),
                    DateParser.parseDate(row.getString("addition_time"))
                ), tgChatId).getFirst();
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
