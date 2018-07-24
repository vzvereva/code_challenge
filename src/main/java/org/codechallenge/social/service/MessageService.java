package org.codechallenge.social.service;

import org.codechallenge.social.repository.MessageRepository;
import org.codechallenge.social.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that handles messages posted by users.
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    /**
     * Creates a new message.
     *
     * @param message new message
     */
    public void postMessage(Message message){
        messageRepository.addMessage(message);
    }
}
