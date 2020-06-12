create table users (
    id serial primary key,
    login varchar(30) not null,
    password varchar(40) not null,
    unique (login)
);