delete from users_roles;
delete from user_table;
delete from role;
-- Insert the 'Administrator' role into the 'role' table
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