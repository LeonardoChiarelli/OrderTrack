create table products(

    id bigint not null auto_increment,
    name varchar(150) not null unique,
    description varchar(255) not null,
    category varchar(100) not null,
    price decimal(6,2) not null,
    active tinyint(1) not null default 1,

    primary key (id)
);