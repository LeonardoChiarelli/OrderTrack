create table payments(

    id bigint not null auto_increment,
    total_price decimal(10,2) not null,
    cardholder_name varchar(150) not null,
    card_number varchar(16) not null,
    expiration_date varchar(5) not null,
    status varchar(50) not null,
    payment_date timestamp not null,
    order_id bigint not null,
    payment_method_id bigint not null,

    primary key (id),
    constraint fk_payments_order_id foreign key (order_id) references orders(id),
    constraint fk_payments_payment_method_id foreign key (payment_method_id) references payment_methods(id)
);