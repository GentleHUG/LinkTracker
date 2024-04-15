package edu.java.scrapper.service;

import edu.java.scrapper.exception.ExistChatException;
import edu.java.scrapper.exception.NotFoundChatException;

public interface TgChatService {
    void register(long tgChatId) throws ExistChatException;
    void unregister(long tgChatId) throws NotFoundChatException;
}
