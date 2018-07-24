package org.codechallenge.social.web;

import org.codechallenge.social.service.FollowerService;
import org.codechallenge.social.web.dto.AddFollowerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that exposes REST API related to operations on user followers.
 */
@RestController
public class FollowerController {

    private final FollowerService followerService;

    @Autowired
    FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @RequestMapping(path = "/users/{userId}/followers", method = RequestMethod.POST)
    public void addFollower(@PathVariable("userId") long userId,
                            @RequestBody AddFollowerRequest addFollowerRequest) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId");
        }
        long followerId = addFollowerRequest.getFollowerId();
        if (followerId <= 0){
            throw new IllegalArgumentException("Invalid followerId");
        }
        followerService.addFollower(followerId, userId);
    }
}
