package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public abstract class AbstractIntegrationTest extends AbstractWebMvcTest {}
