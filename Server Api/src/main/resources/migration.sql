create sequence product_seq
    maxvalue 9999999999999;

alter sequence product_seq owner to postgres;

create sequence product_log_id_seq
    as integer;

alter sequence product_log_id_seq owner to postgres;

alter sequence product_log_id_seq owned by product_log.id;

create sequence auth_user_seq
    maxvalue 9999999999;

alter sequence auth_user_seq owner to postgres;



create table product
(
    id         bigint           not null
        constraint product_pk
            primary key,
    name       varchar(50)      not null,
    price      double precision not null,
    created    timestamp,
    created_by varchar(50),
    updated    timestamp,
    updated_by varchar(50),
    amount     integer          not null
);

alter table product
    owner to postgres;

create trigger product_insert_trigger
    after insert
    on product
    for each row
execute procedure log_product_changes();

create trigger product_update_trigger
    after update
    on product
    for each row
execute procedure log_product_changes();

create trigger product_delete_trigger
    after delete
    on product
    for each row
execute procedure log_product_changes();

create table product_log
(
    id         serial,
    operation  varchar(20),
    product_id bigint,
    name       varchar(255),
    price      double precision,
    amount     integer,
    changed_at timestamp default CURRENT_TIMESTAMP,
    performer  varchar(50)
);

alter table product_log
    owner to postgres;

create table auth_user
(
    id             bigint         not null
        primary key,
    username       varchar(50)
        unique,
    password       varchar(1500)  not null,
    created        timestamp,
    created_by     varchar(50),
    updated        timestamp,
    updated_by     varchar(50),
    role_list      varchar(255)[] not null,
    authority_list varchar(255)[]
);

alter table auth_user
    owner to postgres;

create function log_product_changes() returns trigger
    language plpgsql
as
$$
begin
    if tg_op = 'INSERT' then
        insert into nits_internship.product_log(operation, product_id, name, price, amount, performer)
        values ('CREATED', NEW.id, NEW.name, NEW.price, NEW.amount, NEW.updated_by);
    elseif tg_op = 'UPDATE' then
        insert into nits_internship.product_log(operation, product_id, name, price, amount, performer)
        values ('UPDATED', NEW.id, NEW.name, NEW.price, NEW.amount, NEW.updated_by);
    elseif tg_op = 'DELETE' then
        insert into nits_internship.product_log(operation, product_id, name, price, amount, performer)
        values ('DELETED', OLD.id, OLD.name, OLD.price, OLD.amount, NEW.updated_by);
end if;
return null;
end ;
$$;

alter function log_product_changes() owner to postgres;

