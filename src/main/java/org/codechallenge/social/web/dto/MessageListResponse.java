package org.codechallenge.social.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import org.codechallenge.social.model.Message;

import java.util.List;

/**
 * Response object used to return a list of messages.
 */
@AutoValue
@JsonDeserialize(builder = MessageListResponse.Builder.class)
@JsonSerialize(as = MessageListResponse.class)
public abstract class MessageListResponse {

    /** A list of messages posted previously. */
    @JsonGetter(value = "messages")
    public abstract List<Message> getMessages();

    /** Returns a builder instance that can be used to create a new response instance. */
    public static Builder builder(){
        return Builder.builder();
    }

    /** Builder that provides methods for initializing a new response instance. */
    @AutoValue.Builder
    public abstract static class Builder{

        @JsonCreator
        public static Builder builder() {
            return new AutoValue_MessageListResponse.Builder().setMessages(List.of());
        }

        abstract List<Message> getMessages();

        @JsonSetter("messages")
        public abstract Builder setMessages(List<Message> messages);

        public abstract MessageListResponse autoBuild();

        public MessageListResponse build(){
            return setMessages(List.copyOf(getMessages())).autoBuild();
        }
    }
}
