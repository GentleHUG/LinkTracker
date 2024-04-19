package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowAnswerItem (
    @JsonProperty("creation_date")
    OffsetDateTime creationDate
) {
}
