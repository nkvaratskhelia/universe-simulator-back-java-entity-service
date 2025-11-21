package com.example.universe.simulator.entityservice.smoke;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;

class BeanConfigSmokeTest extends AbstractSmokeTest {

    @Autowired
    private Clock clock;

    @Test
    void test() {
        // clock
        assertThat(clock).isEqualTo(Clock.systemDefaultZone());
    }
}
