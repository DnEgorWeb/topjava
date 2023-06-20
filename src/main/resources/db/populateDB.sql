DELETE
FROM user_role;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, '2023-06-19 10:00:00.000000', 'Завтрак', 500),
       (100000, '2023-06-19 13:00:00.000000', 'Ланч', 1500),
       (100000, '2023-06-19 19:00:00.000000', 'Ужин', 800),
       (100000, '2023-06-20 11:00:00.000000', 'Завтрак', 800),
       (100000, '2023-06-20 15:00:00.000000', 'Полдник', 300);
