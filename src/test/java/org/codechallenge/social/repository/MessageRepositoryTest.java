package org.codechallenge.social.repository;

import org.codechallenge.social.model.Message;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class MessageRepositoryTest {

    private static final Instant NOW = Instant.now();
    private static final Instant INSTANT_1 = Instant.ofEpochMilli(NOW.toEpochMilli() - 4);
    private static final Instant INSTANT_2 = Instant.ofEpochMilli(NOW.toEpochMilli() - 3);
    private static final Instant INSTANT_3 = Instant.ofEpochMilli(NOW.toEpochMilli() - 2);
    private static final Instant INSTANT_4 = Instant.ofEpochMilli(NOW.toEpochMilli() - 1);
    private static final Message MESSAGE_1 = Message.builder()
            .setAuthorId(1)
            .setPostedAt(INSTANT_1)
            .setContent("content1")
            .build();
    private static final Message MESSAGE_2 = Message.builder()
            .setAuthorId(2)
            .setPostedAt(INSTANT_2)
            .setContent("content2")
            .build();
    private static final Message MESSAGE_3 = Message.builder()
            .setAuthorId(1)
            .setPostedAt(INSTANT_3)
            .setContent("content3")
            .build();
    private static final Message MESSAGE_4 = Message.builder()
            .setAuthorId(2)
            .setPostedAt(INSTANT_4)
            .setContent("content4")
            .build();

    private MessageRepository messageRepository;

    @Before
    public void createRepositoryInstance(){
        messageRepository = new MessageRepository();
    }

    @Test
    public void getMessagesAuthoredBy_singleUser_whenNonePosted_returnsEmptyList(){
       List<Message> messages = messageRepository.getMessagesAuthoredBy(1);

        assertThat(messages, empty());
    }

    @Test
    public void getMessagesAuthoredBy_setOfUsers_whenNonePosted_returnsEmptyList(){
        List<Message> messages = messageRepository.getMessagesAuthoredBy(Set.of(1L,2L));

        assertThat(messages, empty());
    }

    @Test
    public void getMessagesAuthoredBy_singleUser_whenSomeWerePosted_returnsMessagesAuthoredByUser(){
        messageRepository.addMessage(MESSAGE_1);
        messageRepository.addMessage(MESSAGE_2);
        messageRepository.addMessage(MESSAGE_3);

        List<Message> messages = messageRepository.getMessagesAuthoredBy(1);

        assertThat(messages, contains(MESSAGE_3, MESSAGE_1));
    }

    @Test
    public void getMessagesAuthoredBy_setOfUsers_whenSomeWerePosted_returnsMessagesAuthoredByUsers(){
        messageRepository.addMessage(MESSAGE_1);
        messageRepository.addMessage(MESSAGE_2);
        messageRepository.addMessage(MESSAGE_3);
        messageRepository.addMessage(MESSAGE_4);

        List<Message> messages = messageRepository.getMessagesAuthoredBy(Set.of(1L,2L));

        assertThat(messages, contains(MESSAGE_4, MESSAGE_3, MESSAGE_2, MESSAGE_1));
    }
}
