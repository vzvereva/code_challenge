package org.codechallenge.social.repository;

import org.codechallenge.social.model.Message;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A repository that stores messages associated with their authors and
 * provides methods to create new messages and to retrieve messages authored by a single user or by a set of users.
 */
@Repository
public class MessageRepository {

    private static final Comparator<Message> MESSAGE_COMPARATOR = Comparator.comparing(Message::getPostedAt).reversed();

    private final ConcurrentHashMap<Long, TreeSet<Message>> authorsToMessages = new ConcurrentHashMap<>();

    /**
     * Adds new message to the repository.
     *
     * @param message new message
     */
    public void addMessage(Message message) {
        authorsToMessages.compute(message.getAuthorId(), (userId, messages) -> {
            TreeSet<Message> newMessages = messages == null ? new TreeSet<>(MESSAGE_COMPARATOR) : messages;
            newMessages.add(message);
            return newMessages;
        });
    }

    /**
     * Retrieves sequential list of messages authored by a given user in reverse chronological order.
     *
     * @param userId user ID
     * @return List of messages posted by the user in reverse chronological order.
     */
    public List<Message> getMessagesAuthoredBy(long userId) {
        return List.copyOf(authorsToMessages.getOrDefault(userId, new TreeSet<>()));
    }

    /**
     * Retrieves sequential list of messages authored by a given set of users in reverse chronological order.
     *
     * @param userIds Set of user IDs
     * @return List of messages posted by the users in reverse chronological order.
     */
    public List<Message> getMessagesAuthoredBy(Set<Long> userIds) {
        return authorsToMessages.entrySet().parallelStream()
                .filter(entry -> userIds.contains(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .sorted(MESSAGE_COMPARATOR)
                .collect(Collectors.toUnmodifiableList());
    }
}
