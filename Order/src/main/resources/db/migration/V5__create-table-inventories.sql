create table inventories(

    id bigint not null auto_increment,
    quantity int not null,
    product_id bigint not null,
    version int not null default 0,

    primary key (id),
    constraint fk_inventories_product_id foreign key (product_id) references products(id)
);