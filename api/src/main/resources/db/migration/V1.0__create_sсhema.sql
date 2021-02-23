create table gift_certificate
(
    id           serial         not null primary key,
    name         varchar(64)    not null,
    description  varchar(512),
    price        numeric(16, 2) not null,
    duration     integer        not null,
    created_date timestamptz    not null default (now() at time zone 'UTC'),
    updated_date timestamptz    not null default (now() at time zone 'UTC'),
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

create table "user"
(
    id       serial       not null primary key,
    password varchar(64)  not null,
    email    varchar(256) not null,
    role     varchar(64)  not null,
    constraint unique_email unique (email)
);

create table "order"
(
    id           serial         not null primary key,
    cost         numeric(16, 2) not null,
    created_date timestamptz    not null default (now() at time zone 'UTC'),
    user_id      integer        not null references "user" on delete cascade
);

create table order_item
(
    id                  serial  not null primary key,
    quantity            integer not null default 1,
    order_id            integer not null references "order" on delete cascade,
    gift_certificate_id integer not null references gift_certificate
);

--AUDIT TABLES
create table revinfo
(
    rev      integer not null primary key,
    revtstmp bigint
);

create table certificate_tag_aud
(
    rev                 integer not null references revinfo,
    gift_certificate_id bigint  not null,
    tag_id              bigint  not null,
    revtype             smallint,
    constraint certificate_tag_aud_pkey
        primary key (rev, gift_certificate_id, tag_id)
);

create table gift_certificate_aud
(
    id           bigint  not null,
    rev          integer not null references revinfo,
    revtype      smallint,
    created_date timestamp,
    description  varchar(255),
    duration     integer,
    name         varchar(255),
    price        numeric(19, 2),
    updated_date timestamp,
    constraint gift_certificate_aud_pkey
        primary key (id, rev)
);

create table order_aud
(
    id           bigint  not null,
    rev          integer not null references revinfo,
    revtype      smallint,
    cost         numeric(16, 2),
    created_date timestamp,
    constraint order_aud_pkey
        primary key (id, rev)
);

create table tag_aud
(
    id      bigint  not null,
    rev     integer not null references revinfo,
    revtype smallint,
    name    varchar(255),
    constraint tag_aud_pkey
        primary key (id, rev)
);