create table gift_certificate
(
    id               serial         not null primary key,
    name             varchar(64)    not null,
    description      varchar(512),
    price            numeric(16, 2) not null,
    duration         integer        not null,
    create_date      timestamptz    not null default (now() at time zone 'UTC'),
    last_update_date timestamptz    not null default (now() at time zone 'UTC'),
    constraint unique_certificate_name unique (name),
    constraint positive_price check (price > (0)::numeric)
);

create table tag
(
    id   serial      not null primary key,
    name varchar(64) not null,
    constraint unique_tag_name unique (name)
);

create table certificate_tag
(
    gift_certificate_id integer not null references gift_certificate on delete cascade,
    tag_id              integer not null references tag on delete cascade,
    primary key (gift_certificate_id, tag_id)
);