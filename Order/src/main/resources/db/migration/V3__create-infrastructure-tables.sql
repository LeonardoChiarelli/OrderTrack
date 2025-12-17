CREATE TABLE shedlock(
     name VARCHAR(64) NOT NULL,
     lock_until TIMESTAMP(3) NOT NULL,
     locked_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
     locked_by VARCHAR(255) NOT NULL,
     PRIMARY KEY (name)
);

CREATE TABLE products_replica(
     id BINARY(16) NOT NULL PRIMARY KEY,
     name VARCHAR(150) NOT NULL,
     price DECIMAL(10,2) NOT NULL,
     active TINYINT(1) NOT NULL
);

CREATE TABLE processed_events(
     message_id VARCHAR(255) NOT NULL PRIMARY KEY,
     processed_at TIMESTAMP NOT NULL
);