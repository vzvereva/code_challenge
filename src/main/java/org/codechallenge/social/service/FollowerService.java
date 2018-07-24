package org.codechallenge.social.service;

import org.codechallenge.social.repository.FollowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * A service that handles follower-followee relationships between the users.
 */
@Service
public class FollowerService {

    private final FollowerRepository followerRepository;

    @Autowired
    FollowerService(FollowerRepository followerRepository){
        this.followerRepository = followerRepository;
    }

    /**
     * Registers a new follower for a followee.
     *
     * @param followerId user ID of the follower
     * @param followeeId user ID of the followee
     */
    public void addFollower(long followerId, long followeeId){
        followerRepository.addFollower(followerId, followeeId);
    }

    /**
     * Retrieves a set of users being followed by the given user.
     *
     * @param userId user ID of the follower
     * @return set of the followee user IDs
     */
    public Set<Long> getFollowees(long userId){
        return followerRepository.getFollowees(userId);
    }
}
