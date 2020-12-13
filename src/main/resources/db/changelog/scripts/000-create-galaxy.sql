CREATE TABLE public.galaxy
(
    id        uuid         NOT NULL,
    "name"    varchar(255) NOT NULL,
    "version" int8         NOT NULL,
    CONSTRAINT galaxy_pkey PRIMARY KEY (id),
    CONSTRAINT uk_q68vq5d83xit86g12ls0ul2uq UNIQUE (name)
);
