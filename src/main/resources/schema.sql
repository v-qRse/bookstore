create table if not exists users (
    id bigint primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(50) not null unique,
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
    user_id bigint not null unique,
    role_id bigint not null
);

create table if not exists roles_privileges (
    role_id bigint not null,
    privilege_id bigint not null
);



create table if not exists Genre (
    id varchar(4) not null,
    description varchar(25) not null,
    type varchar(15) not null unique
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

create table if not exists Status (
    id varchar(4) not null,
    description varchar(30) not null,
    type varchar(15) not null unique
);

create table if not exists Books_Order (
    id bigint primary key,
    status varchar(4) not null,
    delivery_name varchar(50) not null,
    delivery_street varchar(50) not null,
    delivery_city varchar(50) not null,
    delivery_state varchar(2) not null,
    delivery_zip varchar(10) not null,
    cc_number varchar(16) not null,
    cc_expiration varchar(5) not null,
    cc_cvv varchar(3) not null,
    created_at timestamp not null,
    user_id bigint not null
);

create table if not exists Book_On_Order (
    id bigint primary key,
    book bigint not null,
    quantity bigint not null
);

create table if not exists orders_books_on_order (
    order_id bigint not null,
    book_on_order_id bigint not null unique
);



create table if not exists users_orders (
    id bigint primary key,
    user_id bigint not null,
    order_id bigint not null unique
);