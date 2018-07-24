package org.codechallenge.social.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FollowerRepositoryTest {

    private FollowerRepository followerRepository;

    @Before
    public void createRepositoryInstance(){
        followerRepository = new FollowerRepository();
    }

    @Test
    public void getFollowees_whenNoneRegistered_returnsEmptySet(){
        Set<Long> followees = followerRepository.getFollowees(1L);

        assertThat(followees, empty());
    }

    @Test
    public void getFollowees_whenSomeAreRegistered_returnsFollowees(){
        followerRepository.addFollower(1L, 2L);
        followerRepository.addFollower(1L, 3L);

        Set<Long> followees = followerRepository.getFollowees(1L);

        assertThat(followees, containsInAnyOrder(2L, 3L));
    }
}
