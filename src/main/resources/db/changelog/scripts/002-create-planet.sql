create table planet
(
    id      uuid         not null constraint planet_pkey primary key,
    name    varchar(255) not null constraint uk_dhelj2sd5e5spyo2flmdhxo6o unique,
    version bigint       not null,
    star_id uuid         not null constraint fkc4fuaxedn05182lfgl6jadaqi references star
);
