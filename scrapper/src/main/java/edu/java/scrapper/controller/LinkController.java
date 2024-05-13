package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.AddLinkRequest;
import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.ListLinkResponse;
import edu.java.scrapper.controller.dto.RemoveLinkRequest;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.exception.ExistLinkException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.exception.NotFoundLinkException;
import edu.java.scrapper.service.LinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;

@RestController
public class LinkController {
    private final LinkService linkService;

	public LinkController(LinkService linkService) {
		this.linkService = linkService;
	}

	@GetMapping("/links")
    public ResponseEntity<ListLinkResponse> getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId)
        throws NotFoundChatException {
        List<LinkResponse> list = linkService.listAll(tgChatId).stream()
            .map(link -> new LinkResponse(link.id(), URI.create(link.url())))
            .toList();

        return new ResponseEntity<>(new ListLinkResponse(list, list.size()), HttpStatus.OK);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> createLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) throws ExistLinkException, NotFoundChatException {

        Link addedLink = linkService.add(chatId, URI.create(addLinkRequest.link()));
        LinkResponse linkResponse = new LinkResponse(addedLink.id(), URI.create(addedLink.url()));

        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) throws NotFoundLinkException, NotFoundChatException {

        Link removedLink = linkService.remove(tgChatId, URI.create(removeLinkRequest.link()));
        LinkResponse linkResponse = new LinkResponse(removedLink.id(), URI.create(removedLink.url()));

        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }
}
