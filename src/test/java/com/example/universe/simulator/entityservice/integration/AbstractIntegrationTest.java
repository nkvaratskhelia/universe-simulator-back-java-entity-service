package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.AbstractMockMvcTest;
import com.example.universe.simulator.entityservice.common.AbstractSpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AbstractSpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest extends AbstractMockMvcTest {}
