package edu.java.scrapper.service;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.client.dto.GitHubCommitResponse;
import edu.java.scrapper.client.dto.GitHubRepositoryResponse;
import edu.java.scrapper.client.dto.StackOverflowAnswerResponse;
import edu.java.scrapper.client.dto.StackOverflowQuestionResponse;
import edu.java.scrapper.controller.dto.UpdateRequest;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.sender.SenderNotification;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@Log4j2
@Component
public class LinkUpdateScheduler {

    private final LinkUpdater linkUpdater;

    private final GitHubClient gitHubClient;

    private final StackOverflowClient stackOverflowClient;

    private final SenderNotification senderNotification;

    @Value("${app.scheduler.force-check-delay}")
    private Long forceCheckDelay;

    public LinkUpdateScheduler(
        LinkUpdater linkUpdater,
        GitHubClient gitHubClient,
        StackOverflowClient stackOverflowClient,
        SenderNotification senderNotification
    ) {
        this.linkUpdater = linkUpdater;
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
        this.senderNotification = senderNotification;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        log.info("Updating links...");
        Collection<Link> links = linkUpdater.listAllOldCheckedLinks(forceCheckDelay);
        for (Link link: links) {
            checkLink(link);
        }
    }

    private void checkLink(Link link) {
        URI url = URI.create(link.url());
        String host = url.getHost();

        if (host.equals("github.com")) {
            gitHubHandler(link, url);
        } else if (host.equals("stackoverflow.com")) {
            stackOverflowHandler(link, url);
        }
    }

    private void gitHubHandler(Link link, URI url) {
        String[] path = url.getPath().split("/");
        String owner = path[1];
        String repo = path[2];

        try {
            GitHubRepositoryResponse repository = gitHubClient.fetchRepository(owner, repo);
            if (link.lastCheckTime().isBefore(repository.pushedAt())
            || link.lastCheckTime().isBefore(repository.updatedAt())) {
                String description = "(обновление в репозитории)\n";
                linkUpdater.update(link.id());

                List<GitHubCommitResponse> commits = gitHubClient.fetchCommit(owner, repo);
                if (commits.size() != link.commitsCount()) {
                    description += "Новый коммит: " + commits.getFirst().commit().message();
                }

                List<Long> tgChatIds = linkUpdater.listAllTgChatIdByLinkId(link.id());
                sendBotUpdates(link, description, tgChatIds);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void stackOverflowHandler(Link link, URI url) {
        Long questionId = Long.parseLong(url.getPath().split("/")[2]);

        try {

            StackOverflowQuestionResponse.StackOverflowQuestionItem response =
                stackOverflowClient.fetchQuestion(questionId).items().getFirst();
            if (link.lastCheckTime().isBefore(response.lastActivityDate())) {
                String description = "(новая информация по вопросу)\n";
                linkUpdater.update(link.id());

                StackOverflowAnswerResponse answers = stackOverflowClient.fetchAnswer(questionId);
                if (answers.items().size() != link.answerCount()) {
                    description += "Появился новый ответ в " + answers.items().getFirst().creationDate();
                }

                List<Long> tgChatIds = linkUpdater.listAllTgChatIdByLinkId(link.id());
                sendBotUpdates(link, description, tgChatIds);
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void sendBotUpdates(Link link, String description, List<Long> tgChatIds) {
        senderNotification.send(new UpdateRequest(
            link.id(),
            link.url(),
            description,
            tgChatIds
        ));
    }
}
