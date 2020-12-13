CREATE TABLE public.planet
(
    id        uuid         NOT NULL,
    "name"    varchar(255) NOT NULL,
    "version" int8         NOT NULL,
    star_id   uuid         NOT NULL,
    CONSTRAINT planet_pkey PRIMARY KEY (id),
    CONSTRAINT uk_dhelj2sd5e5spyo2flmdhxo6o UNIQUE (name),
    CONSTRAINT fkc4fuaxedn05182lfgl6jadaqi FOREIGN KEY (star_id) REFERENCES star (id)
);
