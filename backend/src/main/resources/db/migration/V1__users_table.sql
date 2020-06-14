create table users (
    id serial primary key,
    login varchar(30) not null,
    password varchar(40) not null,
    full_name varchar(100) not null,
    unique (login)
);