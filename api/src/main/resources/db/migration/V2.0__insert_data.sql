INSERT INTO gift_certificate(id, name, description, price, duration, created_date, updated_date)
VALUES (1, 'goldie''s gym', '5 free visits', 9.99, 7, now() at time zone 'UTC', now() at time zone 'UTC'),
       (2, 'Kfc birthday', '50% off', 5.55, 16, now() at time zone 'UTC', now() at time zone 'UTC'),
       (3, 'Silver screen', 'one film', 4.99, 9, now() at time zone 'UTC', now() at time zone 'UTC');
alter sequence gift_certificate_id_seq restart with 4;

INSERT INTO tag(id, name)
VALUES (1, 'gym'),
       (2, 'cheap'),
       (3, 'rest');
alter sequence tag_id_seq restart with 4;

INSERT INTO certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (3, 3),
       (3, 2);

INSERT INTO "user"(id, username, password, email)
VALUES (1, 'Alex', 'qwerty', 'alex@gamil.com'),
       (2, 'Oleg', 'qwerty', 'oleg@gamil.com');
alter sequence user_id_seq restart with 3;

INSERT INTO role(id, name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO user_role(user_id, role_id)
VALUES (1, 1),
       (2, 2);

