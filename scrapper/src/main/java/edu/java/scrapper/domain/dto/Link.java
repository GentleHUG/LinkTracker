package edu.java.scrapper.domain.dto;

import java.time.OffsetDateTime;

public record Link(
    Long id,
    String url,
    OffsetDateTime additionTime,
    OffsetDateTime lastCheckTime,
    Long answerCount,
    Long commitsCount
){
}
