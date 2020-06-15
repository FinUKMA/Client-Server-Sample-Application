alter table users
    add column role varchar(20) not null;
update users set role = 'admin';