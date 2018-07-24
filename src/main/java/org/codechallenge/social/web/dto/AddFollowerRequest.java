package org.codechallenge.social.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

/**
 * Request object used for registering new followers.
 */
@AutoValue
@JsonDeserialize(builder = AddFollowerRequest.Builder.class)
@JsonSerialize(as = AddFollowerRequest.class)
public abstract class AddFollowerRequest {

   /** User ID of the follower to be registered. */
   @JsonGetter("followerId")
   public abstract long getFollowerId();

   /** Returns a builder instance that can be used to create a new request instance. */
   public static Builder builder() {
      return Builder.builder();
   }

   /** Builder that provides methods for initializing a new request instance. */
   @AutoValue.Builder
   public abstract static class Builder {

      @JsonCreator
      public static Builder builder() {
         return new AutoValue_AddFollowerRequest.Builder();
      }

      @JsonSetter("followerId")
      public abstract Builder setFollowerId(long followerId);

      public abstract AddFollowerRequest build();
   }
}
