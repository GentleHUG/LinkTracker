--liquibase formatter sql

--changeset andeygavrilenko:1
--comment: Create Chats table
create table chats
(
    id bigserial primary key,
    chat_id bigint not null unique,
    addition_time timestamp with time zone default now() not null
);


--changeset andreygavrilenko:2
--comment: Create Links table
create table links
(
    id bigserial primary key,
    url varchar(255) not null unique,
    addition_time timestamp with time zone default now() not null,
    last_check_time timestamp with time zone default now() not null,
    answers_count bigint default 0 not null,
    commits_count bigint default 0 not null
);


--changeset andeygavrelenko:3
--comment: Crete ChatsLinks linking table
create table chats_links
(
    id bigserial primary key,
    chat_id bigint not null references chats (id) ON DELETE CASCADE,
    link_id bigint not null references links (id),
    unique (chat_id, link_id)
);
