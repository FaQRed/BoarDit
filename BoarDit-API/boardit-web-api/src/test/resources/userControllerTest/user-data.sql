-- Удаление существующих данных
DELETE FROM users_roles;
DELETE FROM user_table;
DELETE FROM role;
ALTER TABLE user_table ALTER COLUMN pid RESTART WITH 1;


INSERT INTO role (pid, description)
VALUES ('ROLE_ADMIN', 'Administrator');

-- Insert the user into the 'user_table'
INSERT INTO user_table (first_name, last_name, login, middle_name, password, status)
VALUES ('FirstName', 'LastName', 's@gmail.com', 'MiddleName', '$2a$10$G0GdY7PBP.Jqoaxm73H5Ie0xtmIY7LTtoYwpS.W9IMAXcSTaQ/kKi', 'ACTIVE');

-- Assign the 'Administrator' role to the user in the 'users_roles' table
INSERT INTO users_roles (users_pid, roles_pid)
VALUES (
           (SELECT pid FROM user_table WHERE login = 's@gmail.com'),
           (SELECT pid FROM role WHERE description = 'Administrator')
       );
-- Сброс счетчиков автоинкремента


-- Вставка записей в таблицу role, избегая дублирования
MERGE INTO role AS r
    USING (VALUES
               ('ROLE_ADMIN', 'Administrator role'),
               ('ROLE_USER', 'User role')
    ) AS vals(pid, description)
    ON r.pid = vals.pid
    WHEN NOT MATCHED THEN
        INSERT (pid, description) VALUES (vals.pid, vals.description);

-- Вставка данных пользователей
INSERT INTO user_table (login, password, first_name, middle_name, last_name, status)
VALUES
    ('admin@gmail.com', 'adminpass', 'Admin', NULL, 'User', 'ACTIVE'),
    ('user@gmail.com', 'userpass', 'User', NULL, 'User', 'ACTIVE');

-- Связывание пользователей с ролями
INSERT INTO users_roles (users_pid, roles_pid)
VALUES
    (2, 'ROLE_ADMIN'),
    (3, 'ROLE_USER');