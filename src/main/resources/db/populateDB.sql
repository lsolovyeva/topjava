DELETE FROM user_role;
DELETE FROM users;
DELETE FROM meals;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2023-02-16 09:00:00', 'breakfast only', '1000'),
       (100000, '2023-02-17 12:00:00', 'lunch only', '1000'),
       (100000, '2023-02-18 17:00:00', 'dinner only', '1000'),
       (100001, '2023-02-17 13:00:00', 'dinner', '700'),
       (100001, '2023-02-17 19:00:00', 'supper', '500');
