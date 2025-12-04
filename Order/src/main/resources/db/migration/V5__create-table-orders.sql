CREATE TABLE orders(

    id BINARY(16) NOT NULL PRIMARY KEY,
    consumer_name VARCHAR(150) NOT NULL,
    consumer_email VARCHAR(200) NOT NULL,
    street VARCHAR(150) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL,
    postal_code VARCHAR(9) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state char(2) NOT NULL,
    number int NOT NULL,
    complement VARCHAR(100),
    order_date DATETIME NOT NULL,
    total_price DECIMAL(10,2),
    status VARCHAR(30) NOT NULL,
);