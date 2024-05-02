package edu.java.scrapper.client.dto;

import java.util.List;

public record GitHubCommitResponse (
  String sha,
  Commit commit
){
    public record Commit (
       String message
    ) {
    }
}
