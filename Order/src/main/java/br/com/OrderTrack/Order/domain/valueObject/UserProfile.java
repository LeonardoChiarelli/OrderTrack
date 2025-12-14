package br.com.OrderTrack.Order.domain.valueObject;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import br.com.OrderTrack.Order.domain.profile.Profile;
import br.com.OrderTrack.Order.domain.user.User;

import java.util.Objects;
import java.util.UUID;

public class UserProfile {
    private final UUID userId;
    private final UUID profileId;

    private UserProfile(UUID userId, UUID profileId) {
        if (userId == null || profileId == null) {
            throw new DomainException("All UserProfile core must be provided.");
        }

        this.userId = userId;
        this.profileId = profileId;
    }

    public static UserProfileBuilder builder() {
        return new UserProfileBuilder();
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getProfileId(), that.getProfileId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getProfileId());
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId=" + userId +
                ", profileId=" + profileId +
                '}';
    }

    public static class UserProfileBuilder {
        private UUID userId;
        private UUID profileId;

        public UserProfileBuilder() {}

        public UserProfileBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public UserProfileBuilder profileId(UUID profileId) {
            this.profileId = profileId;
            return this;
        }

        public UserProfile build() {
            if (userId == null || profileId == null) {
                throw new ValidationException("All UserProfile core must be provided.");
            }

            return new UserProfile(this.userId, this.profileId);
        }
    }
}
