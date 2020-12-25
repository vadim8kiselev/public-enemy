package com.kiselev.enemy;

import com.kiselev.enemy.service.PublicEnemyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.kiselev.enemy.data.mongo.repository")
public class PublicEnemyApplication implements CommandLineRunner {

    @Autowired
    private PublicEnemyService publicEnemyService;

    public static void main(String[] args) {
        SpringApplication.run(PublicEnemyApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
