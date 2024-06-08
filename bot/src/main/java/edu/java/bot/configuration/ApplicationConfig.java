package edu.java.bot.configuration;

import edu.java.bot.enums.BackOffType;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    BackOffType backOff,
    List<Integer> retryCodes,
    String topicName,
    String consumerGroupId,
    String bootstrapServer
) {
}
