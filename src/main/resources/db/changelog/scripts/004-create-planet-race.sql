CREATE TABLE public.planet_race (
	planet_id int8 NOT NULL,
	race_id int8 NOT NULL,
	CONSTRAINT planet_race_pkey PRIMARY KEY (planet_id, race_id),
	CONSTRAINT fk40ina209uu4l0gkfgq726gfxj FOREIGN KEY (planet_id) REFERENCES planet(id),
	CONSTRAINT fkb2ehe7ymgh3adqf2t19u69jbi FOREIGN KEY (race_id) REFERENCES race(id)
);
