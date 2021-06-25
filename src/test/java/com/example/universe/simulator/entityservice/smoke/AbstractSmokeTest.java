package com.example.universe.simulator.entityservice.smoke;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractSpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@AbstractSpringBootTest
@AutoConfigureTestDatabase
abstract class AbstractSmokeTest {}
