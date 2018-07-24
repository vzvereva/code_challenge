package org.codechallenge.social.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import org.codechallenge.social.model.Message;

/**
 * Request object used for posting a new message.
 */
@AutoValue
@JsonDeserialize(builder = PostMessageRequest.Builder.class)
@JsonSerialize(as = PostMessageRequest.class)
public abstract class PostMessageRequest {

    /** A new message to be posted.*/
    @JsonGetter("message")
    public abstract Message getMessage();

    /** Returns a builder instance that can be used to create a new request instance. */
    public static Builder builder() {
        return Builder.builder();
    }

    /** Builder that provides methods for initializing a new request instance. */
    @AutoValue.Builder
    public abstract static class Builder {

        @JsonCreator
        public static Builder builder() {
            return new AutoValue_PostMessageRequest.Builder();
        }

        @JsonSetter("message")
        public abstract Builder setMessage(Message message);

        public abstract PostMessageRequest build();
    }
}
