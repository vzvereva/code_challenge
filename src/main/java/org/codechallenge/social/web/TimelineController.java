package org.codechallenge.social.web;

import org.codechallenge.social.model.Message;
import org.codechallenge.social.service.UserContentService;
import org.codechallenge.social.web.dto.MessageListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller that exposes REST API related to operations that retrieve user timeline.
 */
@RestController
public class TimelineController {

    private final UserContentService userContentService;

    @Autowired
    TimelineController(UserContentService userContentService) {
        this.userContentService = userContentService;
    }

    @RequestMapping(path = "/users/{userId}/timeline", method = RequestMethod.GET)
    public MessageListResponse getTimeline(@PathVariable("userId") long userId) {
        if (userId <= 0){
            throw new IllegalArgumentException("Invalid userId");
        }

        List<Message> timeline = userContentService.getTimeline(userId);
        return MessageListResponse.builder().setMessages(timeline).build();
    }
}
