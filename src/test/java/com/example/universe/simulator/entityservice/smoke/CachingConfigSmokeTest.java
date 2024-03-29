package com.example.universe.simulator.entityservice.smoke;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class CachingConfigSmokeTest extends AbstractSmokeTest {

    @Autowired
    private RedisCacheConfiguration cacheConfiguration;

    @Value("${spring.cache.redis.time-to-live}")
    private Duration timeToLive;

    @Test
    void test() {
        assertThat(cacheConfiguration.getTtl())
            .isNotNull()
            .isEqualTo(timeToLive);
        assertThat(cacheConfiguration.getAllowCacheNullValues()).isFalse();
    }
}
