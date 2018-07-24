package org.codechallenge.social.web;

import org.codechallenge.social.model.Message;
import org.codechallenge.social.service.UserContentService;
import org.codechallenge.social.web.dto.MessageListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller that exposes REST API related to operations that retrieve a wall of a user.
 */
@RestController
public class WallController {

    private final UserContentService userContentService;

    @Autowired
    WallController(UserContentService userContentService){
        this.userContentService = userContentService;
    }

    @RequestMapping(path = "/users/{userId}/wall", method = RequestMethod.GET)
    public MessageListResponse getWall(@PathVariable("userId") long userId) {
        if (userId <= 0){
            throw new IllegalArgumentException("Invalid userId");
        }

        List<Message> wall = userContentService.getWall(userId);
        return MessageListResponse.builder().setMessages(wall).build();
    }
}
