create table t_password_reset_requests
(
    "id"     varchar(255) not null constraint t_password_reset_requests_pkey primary key,
    created  timestamp,
    username varchar(255) not null
);

create table t_roles
(
    "id"   bigint       not null constraint t_roles_pkey primary key,
    "name" varchar(255) not null
);

insert into t_roles ("id", "name") values
    (0, 'ROLE_BLOCKED'),
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');

create table t_users
(
    "id"            bigint       not null constraint t_users_pkey primary key,
    email           varchar(255) not null,
    password        varchar(255) not null,
    secret_answer   varchar(255) not null,
    secret_question varchar(255) not null,
    username        varchar(255) not null
);

create table t_articles
(
    "id"             bigint       not null constraint t_articles_pkey primary key,
    content          bytea,
    state            integer      not null,
    title            varchar(255) not null,
    author_id        bigint       constraint references_to_not_null_author references t_users not null,
    publication_date timestamp    not null
);

create table t_users_roles
(
    user_id  bigint not null constraint references_to_not_null_user references t_users not null,
    roles_id bigint not null constraint references_to_not_null_role references t_roles not null,
    constraint t_users_roles_pkey primary key (user_id, roles_id)
);

create table t_user_bookmarks
(
    bookmarked_by_user_id bigint not null constraint references_to_not_null_bookmarked_by_user references t_users not null,
    bookmarked_article_id bigint not null constraint references_to_not_null_bookmarked_article references t_articles not null,
    constraint t_user_bookmarks_pkey primary key (bookmarked_article_id, bookmarked_by_user_id)
);