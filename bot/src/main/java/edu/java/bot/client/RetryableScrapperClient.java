package edu.java.bot.client;

import edu.java.bot.controller.dto.AddLinkRequest;
import edu.java.bot.controller.dto.LinkResponse;
import edu.java.bot.controller.dto.ListLinkResponse;
import edu.java.bot.controller.dto.RemoveLinkRequest;
import org.springframework.retry.support.RetryTemplate;

public class RetryableScrapperClient implements ScrapperClient {

    private final RetryTemplate retryTemplate;
    private final ScrapperClient scrapperClient;

    public RetryableScrapperClient(RetryTemplate retryTemplate, ScrapperClient scrapperClient) {
        this.retryTemplate = retryTemplate;
        this.scrapperClient = scrapperClient;
    }

    @Override
    public void createTgChat(Long id) {
        retryTemplate.execute(context -> {
            scrapperClient.createTgChat(id);
            return null;
        });
    }

    @Override
    public void removeTgChat(Long id) {
        retryTemplate.execute(context -> {
            scrapperClient.removeTgChat(id);
            return null;
        });
    }

    @Override
    public ListLinkResponse getLinks(Long id) {
        return retryTemplate.execute(context -> scrapperClient.getLinks(id));
    }

    @Override
    public LinkResponse createLink(Long id, AddLinkRequest addLinkRequest) {
        return retryTemplate.execute(context -> scrapperClient.createLink(id, addLinkRequest));
    }

    @Override
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return retryTemplate.execute(context -> scrapperClient.deleteLink(chatId, removeLinkRequest));
    }
}
