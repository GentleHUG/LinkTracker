package edu.java.scrapper.client;

import edu.java.scrapper.client.dto.GitHubCommitResponse;
import edu.java.scrapper.client.dto.GitHubRepositoryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import java.util.List;

public interface GitHubClient {
    @GetExchange("repos/{owner}/{repo}")
    GitHubRepositoryResponse fetchRepository(@PathVariable String owner, @PathVariable String repo);

    @GetExchange("repos/{owner]/{repo}/commits")
    List<GitHubCommitResponse> fetchCommit(@PathVariable String owner, @PathVariable String repo);
}
