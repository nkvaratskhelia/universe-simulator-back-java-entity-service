package com.example.universe.simulator.entityservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CachingProperties.class)
@ConditionalOnProperty(prefix = "app.caching", name = "enabled", havingValue = "true")
@EnableCaching
public class CachingConfig {

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(@Value("${spring.cache.redis.time-to-live}") Duration timeToLive) {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(timeToLive)
            .disableCachingNullValues()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
    }

    @Bean
    RedisCacheWriter redisCacheWriter(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
    }

    @Bean
    CacheManager cacheManager(RedisCacheConfiguration redisCacheConfiguration, RedisCacheWriter redisCacheWriter) {
        return RedisCacheManager
            .builder(redisCacheWriter)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }
}
