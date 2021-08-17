create table t_password_reset_requests
(
    "id" varchar(255) not null
        constraint t_password_reset_requests_pkey
            primary key,
    created timestamp,
    username varchar(255) not null
);

alter table t_password_reset_requests owner to postgres;

create table t_roles
(
    "id" bigint not null
        constraint t_roles_pkey
            primary key,
    "name" varchar(255) not null
);

alter table t_roles owner to postgres;

insert into t_roles ("id", "name") values
    (0, 'ROLE_BLOCKED'),
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');

create table t_users
(
    "id" bigint not null
        constraint t_users_pkey
            primary key,
    email varchar(255) not null,
    password varchar(255) not null,
    secret_answer varchar(255) not null,
    secret_question varchar(255) not null,
    username varchar(255) not null
);

alter table t_users owner to postgres;

create table t_articles
(
    "id" bigint not null
        constraint t_articles_pkey
            primary key,
    content bytea,
    state integer not null,
    title varchar(255) not null,
    author_id bigint
        constraint fk271us4rmgo7q8624fmewvfona
            references t_users not null,
    publication_date timestamp not null
);

alter table t_articles owner to postgres;

create table t_users_roles
(
    user_id bigint not null
        constraint fkfxgldwdsgyl221kqaum2l0dm9
            references t_users,
    roles_id bigint not null
        constraint fk9qf4n9htwf2hlfnqoucqmyeg9
            references t_roles,
    constraint t_users_roles_pkey
        primary key (user_id, roles_id)
);

alter table t_users_roles owner to postgres;

create table t_user_bookmarks
(
    bookmarked_article_id bigint not null
        constraint fknqn4pn17e6n3qs3xel3ad3dsc
            references t_users,
    bookmarked_by_user_id bigint not null
        constraint fksp2fkrppo3ssqeu0xpwottx3o
            references t_articles,
    constraint t_user_bookmarks_pkey
        primary key (bookmarked_article_id, bookmarked_by_user_id)
);

alter table t_user_bookmarks owner to postgres;

