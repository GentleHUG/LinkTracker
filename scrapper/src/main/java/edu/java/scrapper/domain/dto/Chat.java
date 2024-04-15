package edu.java.scrapper.domain.dto;

import java.time.OffsetDateTime;

public record Chat(
    Long id,
    Long chatId,
    OffsetDateTime additionTime
) {
}
