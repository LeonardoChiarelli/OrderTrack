create table profiles(

    id bigint not null auto_increment,
    name varchar(50) not null unique,

    primary key(id)
);

insert into profiles values(1, "ROLE_ADMIN");
insert into profiles values(2, "ROLE_CONSUMER")