INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
VALUES ('goldie''s gym', '5 free visits', 9.99, 7, now() at time zone 'UTC', now() at time zone 'UTC'),
       ('Kfc birthday', '50% off', 5.55, 16, now() at time zone 'UTC', now() at time zone 'UTC'),
       ('Silver screen', 'one film', 4.99, 9, now() at time zone 'UTC', now() at time zone 'UTC');

INSERT INTO tag(name)
VALUES ('gym'),
       ('cheap'),
       ('rest');

INSERT INTO certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (3, 3),
       (3, 2);

INSERT INTO "user"(id, username, password, email)
VALUES (1, 'Alex', 'qwerty', 'alex@gamil.com'),
       (2, 'Oleg', 'qwerty', 'oleg@gamil.com');

