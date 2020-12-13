CREATE TABLE public.moon
(
    id        uuid         NOT NULL,
    "name"    varchar(255) NOT NULL,
    "version" int8         NOT NULL,
    planet_id uuid         NOT NULL,
    CONSTRAINT moon_pkey PRIMARY KEY (id),
    CONSTRAINT uk_avhv0qk9t09ywn3hgd59obxaa UNIQUE (name),
    CONSTRAINT fki92drmat85orx06ht6nq5t1a4 FOREIGN KEY (planet_id) REFERENCES planet (id)
);
