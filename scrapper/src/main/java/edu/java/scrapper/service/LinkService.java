package edu.java.scrapper.service;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.exception.ExistLinkException;
import edu.java.scrapper.exception.NotFoundLinkException;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Link add(long tgChatId, URI url) throws NotFoundChatException, ExistLinkException;
    Link remove(long tgChatId, URI url) throws NotFoundChatException, NotFoundLinkException;
    Collection<Link> listAll(long tgChatId) throws NotFoundChatException;
}
