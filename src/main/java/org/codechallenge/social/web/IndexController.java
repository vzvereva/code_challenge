package org.codechallenge.social.web;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResourceSupport getIndex() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(MessageController.class).withRel("messages"));
        index.add(linkTo(FollowerController.class).withRel("users/{userId}/followers"));
        index.add(linkTo(WallController.class).withRel("users/{userId}/wall"));
        index.add(linkTo(TimelineController.class).withRel("users/{userId}/timeline"));
        return index;
    }
}
