package com.example.minor_project1.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * This is a configuration for Redis being used as a cache.
 * Since Cache is a Repo - we will work with the Service layer that would talk to the repository.
 * */
@Configuration
public class CacheConfig {

    /// Get a connection first.
    @Bean
    LettuceConnectionFactory connectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    /// Now we need RedisTemplate to interact with the Redis
    @Bean
    RedisTemplate<String, Object> redisTemplate(){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        return redisTemplate;
    }

}
