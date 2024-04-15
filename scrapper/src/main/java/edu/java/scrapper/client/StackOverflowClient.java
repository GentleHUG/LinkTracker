package edu.java.scrapper.client;

import edu.java.scrapper.client.dto.StackOverflowQuestionResponse;

public interface StackOverflowClient {

    StackOverflowQuestionResponse fetchQuestion(Long questionId);
}
