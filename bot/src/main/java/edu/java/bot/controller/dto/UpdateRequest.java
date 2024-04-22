package edu.java.bot.controller.dto;

import java.util.List;

@SuppressWarnings("LineLength")
public record UpdateRequest(
    Long id,
    String url,
    String description,
    List<Long> tgChatIds
){
}
