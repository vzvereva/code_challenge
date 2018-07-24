package org.codechallenge.social.integration;

import org.assertj.core.util.Maps;
import org.codechallenge.social.model.Message;
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

import java.net.URL;
import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test that checks wall scenarios.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WallIntegrationTest {

    @LocalServerPort
    private int port;

    private URL userWallUrl;
    private URL messagesUrl;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        String baseUrl = "http://localhost:" + port + "/";
        this.userWallUrl = new URL(baseUrl + "users/{userId}/wall");
        this.messagesUrl = new URL(baseUrl + "messages");
    }

    @Test
    public void whenNoMessagesPosted_wallIsEmpty() {
        // Get user's wall and assert it is empty.
        ResponseEntity<MessageListResponse> response = template.getForEntity(userWallUrl.toString(),
                MessageListResponse.class, Maps.newHashMap("userId", 1));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getMessages(), empty());
    }

    @Test
    public void whenMessagePostedByAnotherUser_shouldNotBeShownOnTheWall() {
        // Post message authored by another user
        Message message = Message.builder()
                .setAuthorId(12)
                .setPostedAt(Instant.now())
                .setContent("hello1")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message).build(),
                MessageListResponse.class);

        // Get user's wall and assert it is empty.
        ResponseEntity<MessageListResponse> response = template.getForEntity(userWallUrl.toString(),
                MessageListResponse.class, Maps.newHashMap("userId", 11));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getMessages(), empty());
    }

    @Test
    public void whenMessagesWerePosted_wallContainsAllUserMessagesInReverseOrder() {
        // Post three messages authored by the user.
        Instant now = Instant.now();
        Instant message1PostedAt = Instant.ofEpochMilli(now.toEpochMilli() - 3);
        Message message1 = Message.builder()
                .setAuthorId(13)
                .setPostedAt(message1PostedAt)
                .setContent("hello1")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message1).build(),
                MessageListResponse.class);

        Instant message2PostedAt = Instant.ofEpochMilli(now.toEpochMilli() - 2);
        Message message2 = Message.builder()
                .setAuthorId(13)
                .setPostedAt(message2PostedAt)
                .setContent("hello2")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message2).build(),
                MessageListResponse.class);

        Instant message3PostedAt = Instant.ofEpochMilli(now.toEpochMilli() - 1);
        Message message3 = Message.builder()
                .setAuthorId(13)
                .setPostedAt(message3PostedAt)
                .setContent("hello3")
                .build();
        template.postForEntity(
                messagesUrl.toString(),
                PostMessageRequest.builder().setMessage(message3).build(),
                MessageListResponse.class);

        ResponseEntity<MessageListResponse> response = template.getForEntity(userWallUrl.toString(),
                MessageListResponse.class, Maps.newHashMap("userId", 13));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getMessages(), contains(message3, message2, message1));
    }
}