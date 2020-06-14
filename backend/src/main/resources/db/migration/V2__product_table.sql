create table products (
    id serial primary key,
    name varchar(250) not null,
    price decimal(10, 3) not null,
    total decimal(10, 3) not null
);