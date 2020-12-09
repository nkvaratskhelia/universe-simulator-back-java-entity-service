CREATE TABLE public.moon_race (
	moon_id int8 NOT NULL,
	race_id int8 NOT NULL,
	CONSTRAINT moon_race_pkey PRIMARY KEY (moon_id, race_id),
	CONSTRAINT fk48c3hb471567gkykd0tj1xx5k FOREIGN KEY (race_id) REFERENCES race(id),
	CONSTRAINT fkquxngoiqvsehks2m1kal9uyag FOREIGN KEY (moon_id) REFERENCES moon(id)
);
