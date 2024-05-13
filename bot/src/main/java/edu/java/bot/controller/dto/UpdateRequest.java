package edu.java.bot.controller.dto;

import jakarta.validation.constraints.Pattern;
import java.util.List;

@SuppressWarnings("LineLength")
public record UpdateRequest(
    Long id,
    @Pattern(regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
    String url,
    String description,
    List<Long> tgChatIds
){
}
