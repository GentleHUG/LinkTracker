package edu.java.scrapper.client.dto;

import lombok.Data;
import java.util.List;

@Data
public class StackOverflowQuestionResponse {
    private List<StackOverflowQuestionItem> items;
}
