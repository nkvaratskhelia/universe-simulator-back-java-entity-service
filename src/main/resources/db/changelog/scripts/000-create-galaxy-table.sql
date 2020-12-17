create table galaxy
(
    id      uuid         not null constraint galaxy_pkey primary key,
    name    varchar(255) not null constraint uk_q68vq5d83xit86g12ls0ul2uq unique,
    version bigint       not null
);
