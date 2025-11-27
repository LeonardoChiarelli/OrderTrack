create table payment_methods(

    id bigint not null auto_increment,
    name varchar(75) not null,
    active tinyint(1) not null default 0,

    primary key (id)
);