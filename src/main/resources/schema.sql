create table if not exists users (
    id bigint primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(50) not null,
    password varchar(100) not null
);

create table if not exists Role (
    id bigint primary key,
    name varchar(50) not null
);

create table if not exists Privilege (
    id bigint primary key,
    name varchar(50) not null
);

create table if not exists users_roles (
    user_id bigint not null,
    role_id bigint not null
);

create table if not exists roles_privileges (
   role_id bigint not null,
   privilege_id bigint not null
);



create table if not exists Genre (
    id varchar(4) not null,
    name varchar(25) not null,
    type varchar(15) not null
);

create table if not exists Book (
    id bigint primary key,
    name varchar(50) not null,
    author varchar(50) not null,
    language varchar(20) not null,
    genre varchar(4) not null,--genre id
    publication_year varchar(4) not null,
    description varchar(100) not null,
    isbn varchar(13),
    pages bigint not null,
    rating bigint,
    is_new bit,
    id_icon bigint,
    price bigint not null,
    quantity bigint not null
);

create table if not exists Books_Order (
   id bigint primary key,
   delivery_name varchar(50) not null,
   delivery_street varchar(50) not null,
   delivery_city varchar(50) not null,
   delivery_state varchar(2) not null,
   delivery_zip varchar(10) not null,
   cc_number varchar(16) not null,
   cc_expiration varchar(5) not null,
   cc_cvv varchar(3) not null
);

create table if not exists Book_On_Order (
    id bigint primary key,
    books_order bigint not null,--booksOrder id
    books_order_key bigint not null,--booksOrder id
    book bigint not null,--book id
    quantity bigint not null
);