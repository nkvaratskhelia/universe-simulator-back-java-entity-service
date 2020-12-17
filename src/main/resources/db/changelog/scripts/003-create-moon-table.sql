create table moon
(
    id        uuid         not null constraint moon_pkey primary key,
    name      varchar(255) not null constraint uk_avhv0qk9t09ywn3hgd59obxaa unique,
    version   bigint       not null,
    planet_id uuid         not null constraint fki92drmat85orx06ht6nq5t1a4 references planet
);
