package com.kiselev.enemy.mongo.configuration;

import com.kiselev.enemy.mongo.MongoService;
import com.kiselev.enemy.mongo.repository.MongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.kiselev.enemy.mongo.repository")
public class MongoConfiguration {

    @Bean
    public MongoService mongoClient(MongoRepository mongoRepository) {
        return new MongoService(
                mongoRepository
        );
    }
}
