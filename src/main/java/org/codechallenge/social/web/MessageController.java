package org.codechallenge.social.web;

import com.google.auto.value.AutoValue;
import org.codechallenge.social.model.Message;
import org.codechallenge.social.service.MessageService;
import org.codechallenge.social.web.dto.PostMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Controller that exposes REST API related to operations on messages posted by users.
 */
@RestController
public class MessageController {

    private static final int MESSAGE_LENGTH_LIMIT = 140;

    private final MessageService messageService;

    @Autowired
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/messages", method = RequestMethod.POST)
    public void postMessage(@RequestBody PostMessageRequest postMessageRequest) {
        Message message = postMessageRequest.getMessage();
        long userId = message.getAuthorId();
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId");
        }
        String messageContent = message.getContent();
        if (messageContent.length() > MESSAGE_LENGTH_LIMIT) {
            throw new IllegalArgumentException(String.format("Message length exceeds %d characters", MESSAGE_LENGTH_LIMIT));
        }
        messageService.postMessage(message);
    }
}
