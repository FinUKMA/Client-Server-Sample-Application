INSERT INTO public.users(login, password, full_name, role)
    VALUES ('admin', MD5('password'), 'admin 1 full name', 'admin'),
           ('user', MD5('password'), 'user 1 full name', 'user');