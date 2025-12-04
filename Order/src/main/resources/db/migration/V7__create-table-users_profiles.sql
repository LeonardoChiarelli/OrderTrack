CREATE TABLE users_profiles(

    user_id BINARY(16) NOT NULL,
    profile_id BINARY(16) NOT NULL,

    PRIMARY KEY (user_id, profile_id),
    CONSTRAINT fk_users_profiles_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_users_profiles_profile_id FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

INSERT INTO users_profiles VALUES (1,1);
INSERT INTO users_profiles VALUES (2, 2);