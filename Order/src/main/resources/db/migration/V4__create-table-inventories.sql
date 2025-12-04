CREATE TABLE inventories(

    id BINARY(16) NOT NULL PRIMARY KEY,
    quantity INT NOT NULL,
    product_id BINARY(16) NOT NULL,
    version INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_inventories_product_id FOREIGN KEY (product_id) REFERENCES products(id)
);