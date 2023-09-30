drop schema if exists chat cascade;
drop sequence if exists chat.seq_user;
drop sequence if exists chat.seq_chatroom;
drop sequence if exists chat.seq_message;

create schema if not exists chat;

create table if not exists chat.User (
    ID numeric primary key,
    Login varchar not null unique,
    Password varchar not null
);
CREATE SEQUENCE IF NOT EXISTS chat.seq_user AS BIGINT START WITH 1 INCREMENT BY 1;
ALTER TABLE chat.User ALTER COLUMN ID SET DEFAULT nextval('chat.seq_user');

create table if not exists chat.Chatroom (
    ID numeric primary key,
    Name varchar not null unique,
    Owner numeric not null references chat.User(id)
);
CREATE SEQUENCE IF NOT EXISTS chat.seq_chatroom AS BIGINT START WITH 1 INCREMENT BY 1;
ALTER TABLE chat.Chatroom ALTER COLUMN ID SET DEFAULT nextval('chat.seq_chatroom');

create table if not exists chat.Message (
    ID numeric primary key,
    Author numeric not null references chat.User(id),
    Room numeric not null references chat.Chatroom(id),
    "Text" varchar not null,
    "Date" timestamp default current_timestamp::TIMESTAMP(0)
);
CREATE SEQUENCE IF NOT EXISTS chat.seq_message AS BIGINT START WITH 1 INCREMENT BY 1;
ALTER TABLE chat.Message ALTER COLUMN ID SET DEFAULT nextval('chat.seq_message');