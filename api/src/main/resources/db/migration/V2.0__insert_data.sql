INSERT INTO gift_certificate(id, name, description, price, duration, created_date, updated_date)
VALUES (1, 'goldie''s gym', '5 free visits', 9.99, 7, now() at time zone 'UTC', now() at time zone 'UTC'),
       (2, 'Kfc birthday', '50% off', 5.55, 16, now() at time zone 'UTC', now() at time zone 'UTC'),
       (3, 'Silver screen', 'one film', 4.99, 9, now() at time zone 'UTC', now() at time zone 'UTC');

INSERT INTO tag(id, name)
VALUES (1, 'gym'),
       (2, 'cheap'),
       (3, 'rest');

INSERT INTO certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (3, 3),
       (3, 2);

INSERT INTO "user"(id, username, password, email)
VALUES (1, 'Alex', 'qwerty', 'alex@gamil.com'),
       (2, 'Oleg', 'qwerty', 'oleg@gamil.com');

