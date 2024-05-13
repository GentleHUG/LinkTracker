package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findChatByChatId(Long ChatId);

    void deleteChatByChatId(Long ChatId);
}
