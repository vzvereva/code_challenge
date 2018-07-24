package org.codechallenge.social.service;

import org.codechallenge.social.repository.FollowerRepository;
import org.codechallenge.social.repository.MessageRepository;
import org.codechallenge.social.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * A service that handles different types of content available to a user.
 */
@Service
public class UserContentService {

    private final FollowerRepository followerRepository;
    private final MessageRepository messageRepository;

    @Autowired
    UserContentService(FollowerRepository followerRepository, MessageRepository messageRepository){
        this.followerRepository = followerRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Retrieves a wall, that is a list of messages posted by a given user.
     *
     * @param userId user ID of the author
     * @return a list of messages posted by that user
     */
    public List<Message> getWall(long userId){
        return messageRepository.getMessagesAuthoredBy(userId);
    }

     /**
     * Retrieves a timeline, that is a list of messages posted by all the users followed by a given user.
     *
     * @param userId user ID of the follower
     * @return a list of messages posted by the followees
     */
    public List<Message> getTimeline(long userId){
        Set<Long> followees = followerRepository.getFollowees(userId);
        return followees.isEmpty() ? List.of() : messageRepository.getMessagesAuthoredBy(followees);
    }
}
