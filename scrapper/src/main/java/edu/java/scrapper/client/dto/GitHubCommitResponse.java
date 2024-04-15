package edu.java.scrapper.client.dto;

import java.util.List;

public record GitHubCommitResponse (
    List<GitHumCommitItem> items
){
    public record GitHumCommitItem(
        String sha,
        Commit commit
    ) {
        public record Commit (
            String message
        ) {
        }
    }
}
