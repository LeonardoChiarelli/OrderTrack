create table users(

    id bigint not null auto_increment,
    name varchar(150) not null,
    email varchar(200) not null unique,
    password varchar(255) not null,
    profile_id bigint not null,

    primary key(id),
    constraint fk_users_profile_id foreign key (profile_id) references profiles(id)
);

insert into users values (1, "ADMIN", "admin@gmail.com", "12345", 1);
insert into users values (1, "CONSUMER", "consumer@gmail.com", "12345", 2);