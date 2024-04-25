package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepositoryNew;
import edu.java.scrapper.service.LinkUpdater;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    JdbcLinkRepositoryNew jdbcLinkRepository;

    public JdbcLinkUpdater(JdbcLinkRepositoryNew jdbcLinkRepository) {
        this.jdbcLinkRepository = jdbcLinkRepository;
    }

    @Override
    public int update(Long linkId) {
        return jdbcLinkRepository.updateLastCheckTime(linkId);
    }

    @Override
    public Collection<Link> listAllOldCheckedLinks(Long forceCheckDelay) {
        return jdbcLinkRepository.findOldCheckedLinks(forceCheckDelay);
    }

    @Override
    public List<Long> listAllTgChatIdByLinkId(Long linkId) {
        return jdbcLinkRepository.findAllChatIdsByLinkId(linkId);
    }
}
