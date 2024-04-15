package edu.java.bot.controller.dto;

import java.util.List;

@SuppressWarnings("LineLength")
public record UpdateRequest(
    Integer id,
    String url,
    String description,
    List<Integer> tgChatIds
){
}
