package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class GitHubRepositoryResponse {
    private long id;
    private String name;

    @JsonProperty("pushed_at")
    private OffsetDateTime pushedAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}
