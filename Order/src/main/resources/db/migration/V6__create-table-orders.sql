create table orders(

    id bigint not null auto_increment,
    consumer_name varchar(150) not null,
    consumer_email varchar(200) not null,
    street varchar(150) not null,
    neighborhood varchar(100) not null,
    postal_code varchar(9) not null,
    city varchar(100) not null,
    state char(2) not null,
    number int not null,
    complement varchar(100),
    order_date datetime not null,
    total_price decimal(10,2),
    status varchar(30) not null,

    primary key (id)
);