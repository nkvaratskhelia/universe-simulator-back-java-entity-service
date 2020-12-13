CREATE TABLE public.star (
	id uuid NOT NULL,
	"name" varchar(255) NOT NULL,
	"version" int8 NOT NULL,
	galaxy_id uuid NOT NULL,
	CONSTRAINT star_pkey PRIMARY KEY (id),
	CONSTRAINT uk_64qlrdcru0rp18k6u1bs6feav UNIQUE (name),
	CONSTRAINT fkol8sl9hdlkm9u7pv4154dbx1g FOREIGN KEY (galaxy_id) REFERENCES galaxy(id)
);
