package br.com.OrderTrack.Order.domain.profile;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

public class Profile {
    private final UUID id;
    private final String name;

    private Profile(String name) {
        if (name.isBlank()) {
            throw new DomainException("All Profile core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.name = name;
    }

    public static ProfileBuilder builder() {
        return new ProfileBuilder();
    }

    public boolean isAdmin() { return this.name.equals("ROLE_ADMIN"); }
    public boolean isConsumer() { return this.name.equals("ROLE_CONSUMER"); }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class ProfileBuilder {
        private String name;

        public ProfileBuilder() {}

        public ProfileBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Profile build() {
            if (name.isBlank()) {
                throw new ValidationException("All Profile core must be provided.");
            }

            return new Profile(this.name);
        }
    }
}
