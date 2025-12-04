CREATE TABLE order_items(

    id BINARY(16) NOT NULL PRIMARY KEY,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL,
    unit_price_at_purchase DECIMAL(6,2) NOT NULL,
    order_id BINARY(16) NOT NULL,

    constraint fk_order_items_product_id foreign key (product_id) references products(id),
    constraint fk_order_items_order_id foreign key (order_id) references orders(id)
);