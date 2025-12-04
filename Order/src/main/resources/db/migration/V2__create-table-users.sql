CREATE TABLE users(

    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_id BINARY(16) NOT NULL,

    CONSTRAINT fk_users_profile_id FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

INSERT INTO users VALUES (1, "ADMIN", "admin@gmail.com", "12345", 1);
INSERT INTO users VALUES (1, "CONSUMER", "consumer@gmail.com", "12345", 2);