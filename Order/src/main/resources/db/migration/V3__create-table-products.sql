CREATE TABLE products(

    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(150) NOT NULL unique,
    description  VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DECIMAL(6,2) NOT NULL,
    active tinyint(1) NOT NULL DEFAULT 1,
);