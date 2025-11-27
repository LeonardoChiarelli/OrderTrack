create table order_items(

    id bigint not null auto_increment,
    product_id bigint not null,
    quantity int not null,
    unit_price decimal(6,2) not null,
    order_id bigint not null,

    primary key (id),
    constraint fk_order_items_product_id foreign key (product_id) references products(id),
    constraint fk_order_items_order_id foreign key (order_id) references orders(id)
);