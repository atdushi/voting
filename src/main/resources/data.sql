INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100002);

INSERT INTO restaurant (name)
VALUES ('ТОКИО-CITY'),
       ('Bahroma');

INSERT INTO dish (name, price, restaurant_id, date)
VALUES ('лазанья 1', 300.5, 100003, '2020-01-30'),
       ('лазанья 2', 300.5, 100003, '2020-01-30'),
       ('лазанья 3', 300.5, 100003, '2020-01-30'),
       ('лазанья 4', 300.5, 100003, '2020-01-30'),
       ('шашлык', 400.0, 100004, '2020-01-30');

INSERT INTO vote (user_id, restaurant_id, created)
VALUES (100000, 100003, '2020-01-30'),
       (100001, 100003, '2020-01-30');