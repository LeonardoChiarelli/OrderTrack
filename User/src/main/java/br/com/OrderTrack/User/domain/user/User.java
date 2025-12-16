package br.com.OrderTrack.User.domain.user;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.User.domain.profile.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private List<Profile> profiles = new ArrayList<>();

    private User(String name,
                 String email,
                 String password,
                 List<Profile> profiles) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || profiles.isEmpty()) {
            throw new DomainException("All user core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.profiles = profiles;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Boolean isAdmin() {
        return this.profiles.stream()
            .anyMatch(Profile::isAdmin);
    }

    public Boolean isConsumer() {
        return this.profiles.stream()
                .anyMatch(Profile::isConsumer);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profiles=" + profiles +
                '}';
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private String password;
        private List<Profile> profiles = new ArrayList<>();

        public UserBuilder() {}

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder profile(List<Profile> profiles) {
            this.profiles = profiles;
            return this;
        }

        public User build() {
            if (name.isBlank() || email.isBlank() || password.isBlank() || profiles.isEmpty()) {
                throw new ValidationException("All User core must be provided.");
            }

            return new User(
                    this.name,
                    this.email,
                    this.password,
                    this.profiles
            );
        }
    }
}
