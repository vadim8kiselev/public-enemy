package com.kiselev.enemy;

import com.kiselev.enemy.instagram.InstagramService;
import com.kiselev.enemy.mongo.MongoService;
import com.kiselev.enemy.service.PublicEnemy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PublicEnemyConfiguration {

    @Value("${com.kiselev.enemy.usernames}")
    private List<String> usernames;

    @Bean
    public PublicEnemy publicEnemy(InstagramService instagramService,
                                   MongoService mongoService) {
        return new PublicEnemy(
                instagramService,
                mongoService,
                usernames
        );
    }
}
