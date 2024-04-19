package edu.java.scrapper.client.dto;

import java.util.List;

public record StackOverflowAnswerResponse(
    List<StackOverflowAnswerItem> items
) {
}
