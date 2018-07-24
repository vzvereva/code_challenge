package org.codechallenge.social.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import java.time.Instant;

/**
 * Model object that represents a single message posted by a user of the social application.
 */
@AutoValue
@JsonDeserialize(builder = Message.Builder.class)
@JsonSerialize(as = Message.class)
public abstract class Message {

    /** User ID of the message author. */
    @JsonGetter("authorId")
    public abstract long getAuthorId();

    /** The time when message was posted. */
    @JsonGetter("postedAt")
    public abstract Instant getPostedAt();

    /** The content of the message. */
    @JsonGetter("content")
    public abstract String getContent();

    /** Returns a builder instance that can be used to create a new message instance. */
    public static Builder builder(){
        return Builder.builder();
    }

    /** Builder that provides methods for initializing a new message instance. */
    @AutoValue.Builder
    public abstract static class Builder {

        @JsonCreator
        public static Builder builder() {
            return new AutoValue_Message.Builder();
        }

        @JsonSetter("authorId")
        public abstract Builder setAuthorId(long authorId);

        @JsonSetter("postedAt")
        public abstract Builder setPostedAt(Instant postedAt);

        @JsonSetter("content")
        public abstract Builder setContent(String content);

        public abstract Message build();
    }
}
