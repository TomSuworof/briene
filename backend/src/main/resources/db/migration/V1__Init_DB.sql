create table if not exists t_password_reset_requests
(
    id       uuid         not null constraint t_password_reset_requests_pkey primary key,
    created  timestamp,
    username varchar(255) not null
);

create table if not exists t_roles
(
    id   bigint       not null constraint t_roles_pkey primary key,
    name varchar(255) not null
);

insert into t_roles (id, name) values
    (0, 'ROLE_BLOCKED'),
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER')
on conflict(id) do update set id = excluded.id, name = excluded.name;

create table if not exists t_users
(
    id              uuid         not null constraint t_users_pkey primary key,
    email           varchar(255) not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    bio             varchar(255)
);

create table if not exists t_articles
(
    id               uuid                     not null constraint t_articles_pkey primary key,
    content          text                     not null,
    state            integer                  not null,
    summary          text,
    title            varchar(255)             not null,
    author_id        uuid                     not null constraint references_to_not_null_author references t_users not null,
    publication_date timestamp with time zone not null,
    url              text                     not null
);

create table if not exists t_users_roles
(
    user_id  uuid   not null constraint references_to_not_null_user references t_users not null,
    roles_id bigint not null constraint references_to_not_null_role references t_roles not null,
    constraint t_users_roles_pkey primary key (user_id, roles_id)
);

create table if not exists t_user_bookmarks
(
    bookmarked_by_user_id uuid not null constraint references_to_not_null_bookmarked_by_user references t_users    not null,
    bookmarked_article_id uuid not null constraint references_to_not_null_bookmarked_article references t_articles not null,
    constraint t_user_bookmarks_pkey primary key (bookmarked_article_id, bookmarked_by_user_id)
);

create table t_tags
(
    id   uuid         not null primary key,
    name varchar(255) not null
);

create table t_articles_tags
(
    article_id uuid not null constraint references_to_not_null_article_id references t_articles,
    tags_id    uuid not null constraint references_to_not_null_tags_is references t_tags
);
