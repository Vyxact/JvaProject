drop database if exists jproj;
create database jproj with owner "kv.kn";

create extension if not exists "uuid-ossp";

create table if not exists branches
(
    branch_id uuid         not null,
    branch    varchar(100) not null,
    constraint branches_pkey
        primary key (branch_id)
);

alter table branches
    owner to "kv.kn";

insert into branches values (uuid_generate_v4(), 'branch1');
insert into branches values (uuid_generate_v4(), 'branch2');
insert into branches values (uuid_generate_v4(), 'branch3');

create table if not exists customers
(
    customer_id uuid         not null,
    branch_id   uuid         not null,
    firstname   varchar(100) not null,
    lastname    varchar(50)  not null,
    username    varchar(50)  not null,
    password    varchar(512) not null,
    city        varchar(20)  not null,
    contact     varchar(20)  not null,
    constraint customers_pkey
        primary key (customer_id),
    constraint customers_branch_id_fkey
        foreign key (branch_id) references branches
            on update cascade on delete cascade
);

alter table customers
    owner to "kv.kn";

create table if not exists accounts
(
    acc_id        uuid        not null,
    customer_id   uuid        not null,
    card_number   text        not null,
    acc_type      varchar(50) not null,
    balance       numeric(10, 2) default 0.00,
    creation_date timestamp      default CURRENT_TIMESTAMP,
    constraint accounts_pkey
        primary key (acc_id),
    constraint accounts_customer_id_fkey
        foreign key (customer_id) references customers
            on update cascade on delete cascade
);

alter table accounts
    owner to "kv.kn";

create unique index if not exists accounts_card_number_uindex
    on accounts (card_number);

alter table accounts
    add constraint accounts_balance_check
        check (balance >= (0)::numeric);

create table if not exists deposits
(
    date       date           default CURRENT_DATE,
    time       time           default now(),
    transac_id uuid not null,
    acc_id     uuid not null,
    cust_id    uuid not null,
    deposit    numeric(10, 2) default 0.00,
    balance    numeric(10, 2) default 0.00,
    constraint deposits_pkey
        primary key (transac_id),
    constraint deposits_acc_id_fkey
        foreign key (acc_id) references accounts
            on update cascade on delete cascade,
    constraint deposits_cust_id_fkey
        foreign key (cust_id) references customers
            on update cascade on delete cascade
);

alter table deposits
    owner to "kv.kn";

create table if not exists history
(
    id      uuid not null,
    acc_id  uuid,
    message varchar(255),
    status  varchar(20),
    date    date default CURRENT_DATE,
    time    time default now(),
    constraint history_pkey
        primary key (id),
    constraint acc_id
        foreign key (acc_id) references accounts
            on update cascade
);

alter table history
    owner to "kv.kn";

create table if not exists transfers
(
    balance     numeric(10, 2) default 0.00,
    date        date           default CURRENT_DATE,
    time        time           default now(),
    transfer_id uuid not null,
    acc_from    text not null,
    acc_to      text not null,
    amount      numeric(10, 2) default 0.00,
    constraint transfers_pkey
        primary key (transfer_id),
    constraint transfers_fk
        foreign key (acc_from) references accounts (card_number)
            on update cascade
);

alter table transfers
    owner to "kv.kn";

create table if not exists withdrawals
(
    date       date default CURRENT_DATE,
    time       time default now(),
    transac_id uuid not null,
    acc_id     uuid not null,
    cust_id    uuid not null,
    withdrawal numeric(10, 2),
    balance    numeric(10, 2),
    constraint withdrawals_pkey
        primary key (transac_id),
    constraint withdrawals_acc_id_fkey
        foreign key (acc_id) references accounts
            on update cascade on delete cascade,
    constraint withdrawals_cust_id_fkey
        foreign key (cust_id) references customers
            on update cascade on delete cascade
);

alter table withdrawals
    owner to "kv.kn";
