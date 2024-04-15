package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class StackOverflowQuestionItem {

    @JsonProperty("question_id")
    private long questionId;

    @JsonProperty("is_answered")
    private boolean isAnswered;

    @JsonProperty("view_count")
    private int viewCount;

    private int score;

    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;

    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;
}
