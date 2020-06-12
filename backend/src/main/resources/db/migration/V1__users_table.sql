create table `users` (
    `id` bigint auto_increment primary key,
    `login` varchar(30) not null,
    `password` varchar(40) not null,
    unique `uniq_login` (login)
);