package edu.java.scrapper.controller.dto;

import java.util.List;

@SuppressWarnings("LineLength")
public record UpdateRequest(
    Long id,
    String url,
    String description,
    List<Long> tgChatIds
){
}
