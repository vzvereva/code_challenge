package org.codechallenge.social.integration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URL;
import java.time.Instant;

import org.assertj.core.util.Maps;
import org.codechallenge.social.model.Message;
import org.codechallenge.social.web.dto.AddFollowerRequest;
import org.codechallenge.social.web.dto.MessageListResponse;
import org.codechallenge.social.web.dto.PostMessageRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test that checks timeline scenarios.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelineIntegrationTest {

    @LocalServerPort
    private int port;

    private URL userFollowersUrl;
    private URL userTimelineUrl;
    private URL messagesUrl;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        String baseUrl = "http://localhost:" + port + "/";
        this.userFollowersUrl = new URL(baseUrl + "users/{userId}/followers");
        this.userTimelineUrl = new URL(baseUrl + "users/{userId}/timeline");
        this.messagesUrl = new URL(baseUrl + "messages");
    }

    @Test
    public void whenNoUsersAreFollowed_timelineIsEmpty() {
        // Post message authored by another user.
        Message message = Message.builder()
                .setAuthorId(22)
                .setPostedAt(Instant.now())
                .setContent("hello")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message).build(),
                MessageListResponse.class);

        // Get user's timeline and assert it is empty.
        ResponseEntity<MessageListResponse> response = template.getForEntity(userTimelineUrl.toString(),
                MessageListResponse.class, Maps.newHashMap("userId", 1));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getMessages(), empty());
    }

    @Test
    public void ownMessages_shouldNotBeShownInTimeline() {
        // Post message authored by the same user.
        Message message = Message.builder()
                .setAuthorId(21)
                .setPostedAt(Instant.now())
                .setContent("hello")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message).build(),
                MessageListResponse.class);

        // Get user's timeline and assert it is empty.
        ResponseEntity<MessageListResponse> response = template.getForEntity(userTimelineUrl.toString(),
                MessageListResponse.class, Maps.newHashMap("userId", 21));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getMessages(), empty());
    }

    @Test
    public void whenUsersAreFollowed_wallContainsAllRelevantMessagesInReverseOrder() {
        // Post 3 messages authored by two other users.
        Instant now = Instant.now();
        Instant message1PostedAt = Instant.ofEpochMilli(now.toEpochMilli() - 3);
        Message message1 = Message.builder()
                .setAuthorId(24)
                .setPostedAt(message1PostedAt)
                .setContent("hello1")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message1).build(),
                MessageListResponse.class);

        Instant message2PostedAt = Instant.ofEpochMilli(now.toEpochMilli() - 2);
        Message message2 = Message.builder()
                .setAuthorId(25)
                .setPostedAt(message2PostedAt)
                .setContent("hello2")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message2).build(),
                MessageListResponse.class);

        Instant message3PostedAt = Instant.ofEpochMilli(now.toEpochMilli() - 1);
        Message message3 = Message.builder()
                .setAuthorId(24)
                .setPostedAt(message3PostedAt)
                .setContent("hello3")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message3).build(),
                MessageListResponse.class);

        // Start following those two users.
        template.postForEntity(
                userFollowersUrl.toString(),
                AddFollowerRequest.builder().setFollowerId(23).build(),
                MessageListResponse.class,
                Maps.newHashMap("userId", 24));
        template.postForEntity(
                userFollowersUrl.toString(),
                AddFollowerRequest.builder().setFollowerId(23).build(),
                MessageListResponse.class,
                Maps.newHashMap("userId", 25));

        // Get user's timeline and assert it contains all three messages in reverse chronological order.
        ResponseEntity<MessageListResponse> response = template.getForEntity(userTimelineUrl.toString(),
                MessageListResponse.class, Maps.newHashMap("userId", 23));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getMessages(), contains(message3, message2, message1));
    }
}