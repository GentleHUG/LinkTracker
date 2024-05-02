package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.OffsetDateTime;


public record GitHubRepositoryResponse (
    long id,

    String name,

    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt,

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt
) {
}
