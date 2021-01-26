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

create table certificate_x_tag
(
    gift_certificate_id integer not null references gift_certificate on delete cascade,
    tag_id              integer not null references tag on delete cascade,
    primary key (gift_certificate_id, tag_id)
);

create table consumer
(
    id    serial      not null primary key,
    login varchar(64) not null,
    constraint unique_login unique (login)
);

create table purchase
(
    id          serial      not null primary key,
    placed_date timestamptz not null default (now() at time zone 'UTC'),
    consumer_id integer     not null references consumer on delete cascade
);

create table gift_certificate_as_purchase_item
(
    id          serial         not null primary key,
    name        varchar(64)    not null,
    description varchar(512),
    price       numeric(16, 2) not null,
    duration    integer        not null,
    purchase_id integer        not null references purchase on delete cascade,
    constraint positive_price check (price > (0)::numeric)
);