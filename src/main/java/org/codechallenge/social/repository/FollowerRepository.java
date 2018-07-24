package org.codechallenge.social.repository;

import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A repository that stores follower-followee relationships between the users and
 * provides methods to create and retrieve such relationships.
 */
@Repository
public class FollowerRepository {

    private final ConcurrentHashMap<Long, Set<Long>> followersToFollowees = new ConcurrentHashMap<>();

    /**
     * Adds new follower-followee relationship between the users.
     *
     * @param followerId user ID of the follower
     * @param followeeId user ID of the followee
     */
    public void addFollower(long followerId, long followeeId) {
        followersToFollowees.compute(followerId, (userId, followeeIds) -> {
            Stream<Long> followeeIdsStream = followeeIds == null ? Stream.empty() : followeeIds.stream();
            return Stream.concat(followeeIdsStream, Stream.of(followeeId)).collect(Collectors.toUnmodifiableSet());
        });
    }

    /**
     * Gets a set of users being followed by the given user.
     *
     * @param followerId user ID of the follower
     * @return set of the followee user IDs
     */
    public Set<Long> getFollowees(long followerId) {
        return followersToFollowees.getOrDefault(followerId, Set.of());
    }
}
