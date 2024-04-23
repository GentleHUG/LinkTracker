package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.dto.Link;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Primary
public class JdbcLinkRepositoryNew {
    private JdbcTemplate jdbcTemplate;
    private LinkRowMapper linkRowMapper;

    public JdbcLinkRepositoryNew(JdbcTemplate jdbcTemplate, LinkRowMapper linkRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.linkRowMapper = linkRowMapper;
    }

    public List<Link> findAll() {
        String sql = "SELECT * FROM links";
        return jdbcTemplate.query(sql, linkRowMapper);
    }

    public Link findLinkByUrl(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        return jdbcTemplate.queryForObject(sql, linkRowMapper, url);
    }

    public List<Link> findAllByChatId(Long chatId) {
        String sql = """
            SELECT links.*
            FROM links
            JOIN chats_links
            ON links.id = chats_links.link_id
            WHERE chats_links.chat_id = ?
            """;

        return jdbcTemplate.query(sql, linkRowMapper, chatId);
    }

    public void add(String url) {
        String sql = "INSERT INTO links (url) VALUES (?)";
        jdbcTemplate.update(sql, url);
    }

    public void addLinkIdToChatId(Long linkId, Long ChatId) {
        String sql = "INSERT INTO chats_links (link_id, chat_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, linkId, ChatId);
    }

    public boolean isLinkConnectedToChat(Long chatId, Long linkId) {
        String sql = "SELECT COUNT(*) FROM chats_links WHERE chat_id = ? AND link_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, chatId, linkId).equals(0);
    }

    public boolean isLinkConnected(Long linkId) {
        String sql = "SELECT COUNT(*) FROM chats_links WHERE link_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, linkId) > 0;
    }

    public void removeLinkToChat(Long chatId, Long linkId) {
        String sql = "DELETE FROM chats_links WHERE chat_id = ? AND link_id = ?";
        jdbcTemplate.update(sql, linkId);
    }

    public void removeLink(Long linkId) {
        String sql = "DELETE FROM links WHERE id = ?";
        jdbcTemplate.update(sql, linkId);
    }
}
