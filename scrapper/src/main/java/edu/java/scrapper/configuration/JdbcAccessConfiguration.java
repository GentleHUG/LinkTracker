package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jdbc.JdbcChatRepositoryNew;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepositoryNew;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public ChatService chatService(JdbcChatRepositoryNew jdbcChatRepositoryNew) {
        return new JdbcChatService(jdbcChatRepositoryNew);
    }

    @Bean
    public LinkService linkService(JdbcLinkRepositoryNew jdbcLinkRepositoryNew, JdbcChatRepositoryNew jdbcChatRepositoryNew) {
        return new JdbcLinkService(jdbcLinkRepositoryNew, jdbcChatRepositoryNew);
    }

    @Bean
    public LinkUpdater linkUpdater(JdbcLinkRepositoryNew jdbcLinkRepositoryNew) {
        return new JdbcLinkUpdater(jdbcLinkRepositoryNew);
    }
}
