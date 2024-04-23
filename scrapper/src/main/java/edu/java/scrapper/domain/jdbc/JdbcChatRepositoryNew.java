package edu.java.scrapper.domain.jdbc;


import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.exception.ExistChatException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.service.TgChatService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Primary
public class JdbcChatRepositoryNew {

    JdbcTemplate jdbcTemplate;
    ChatRowMapper chatRowMapper;

    public JdbcChatRepositoryNew(JdbcTemplate jdbcTemplate, ChatRowMapper chatRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.chatRowMapper = chatRowMapper;
    }

    public List<Chat> findAll() {
        String sql = "select * from chats";
        return jdbcTemplate.query(sql, chatRowMapper);
    }

    public List<Chat> findAllByLinkId(String linkId) {
        String sql = """
            SELECT chats.*
            FROM chats
            JOIN chats_links
            ON chats.id = chats_links.chat_id
            WHERE chats_links.link_id = ?
            """;

        return jdbcTemplate.query(sql, chatRowMapper, linkId);
    }

    public Chat findChatByTgChatId(Long tgChatId) {
        String sql = "SELECT * FROM chats WHERE chat_id = ?";
        return jdbcTemplate.queryForObject(sql, chatRowMapper, tgChatId);
    }

    public Chat findChatById(Long chatId) {
        String sql = "SELECT * FROM chats WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, chatRowMapper, chatId);
    }

    public void add(Long tgChatId) {
        String sql = "INSERT INTO chats (chat_id) VALUES (?)";
        jdbcTemplate.update(sql, tgChatId);
    }

    public boolean remove(Long chatId) {
        String sql = "DELETE FROM chats WHERE id = ?";
        return jdbcTemplate.update(sql, chatId) > 0;
    }
}
