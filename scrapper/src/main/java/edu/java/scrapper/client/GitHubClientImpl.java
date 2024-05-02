package edu.java.scrapper.client;

import edu.java.scrapper.client.dto.GitHubCommitResponse;
import edu.java.scrapper.client.dto.GitHubRepositoryResponse;
import edu.java.scrapper.configuration.ApplicationConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GitHubClientImpl implements GitHubClient {
    private final WebClient webClient;

    public GitHubClientImpl(ApplicationConfig applicationConfig) {
        this.webClient = WebClient.builder()
            .baseUrl(applicationConfig.urls().gitHubBaseUrl())
            .build();
    }

    @Override
    public GitHubRepositoryResponse fetchRepository(String owner, String repo) {
        return webClient.get()
            .uri("repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class)
            .block();
    }

    @Override
    public List<GitHubCommitResponse> fetchCommit(String owner, String repo) {
        try {
            GitHubCommitResponse[] commits =
                webClient.get()
                .uri("repos/{owner}/{repo}/commits", owner, repo)
                .retrieve()
                .bodyToMono(GitHubCommitResponse[].class)
                .block();

            return Arrays.stream(commits).toList();

        } catch (Exception ex) {
            return new ArrayList<>(0);
        }
    }
}
