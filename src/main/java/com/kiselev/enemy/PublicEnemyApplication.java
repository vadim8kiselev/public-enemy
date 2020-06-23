package com.kiselev.enemy;

import com.kiselev.enemy.service.PublicEnemy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PublicEnemyApplication implements CommandLineRunner {

    @Autowired
    private PublicEnemy publicEnemy;

    public static void main(String[] args) {
        SpringApplication.run(PublicEnemyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        publicEnemy
                .operate();
    }
}
