create table star
(
    id        uuid         not null constraint star_pkey primary key,
    name      varchar(255) not null constraint uk_64qlrdcru0rp18k6u1bs6feav unique,
    version   bigint       not null,
    galaxy_id uuid         not null constraint fkol8sl9hdlkm9u7pv4154dbx1g references galaxy
);
