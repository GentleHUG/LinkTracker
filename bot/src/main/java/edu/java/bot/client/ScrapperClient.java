package edu.java.bot.client;

import edu.java.bot.controller.dto.AddLinkRequest;
import edu.java.bot.controller.dto.LinkResponse;
import edu.java.bot.controller.dto.ListLinkResponse;
import edu.java.bot.controller.dto.RemoveLinkRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;


public interface ScrapperClient {
    @PostExchange("/tg-chat/{id}")
    void createTgChat(@PathVariable Long id);

    @DeleteExchange("/tg-chat/{id}")
    void removeTgChat(@PathVariable Long id);

    @GetExchange("/links")
    ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") Long id);

    @PostExchange("/links")
    LinkResponse createLink(
        @RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody AddLinkRequest addLinkRequest
    );

    @DeleteExchange("/links")
    LinkResponse deleteLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    );
}
