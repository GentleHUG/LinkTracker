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
    public static Link createFromJpa(edu.java.scrapper.domain.jpa.entity.Link link) {
        return new Link(
            link.getId(),
            link.getUrl(),
            link.getAdditionTime(),
            link.getLastCheckTime(),
            link.getAnswersCount(),
            link.getCommitsCount()
        );
    }
}
