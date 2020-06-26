package com.kiselev.enemy.service;

import com.kiselev.enemy.instagram.InstagramService;
import com.kiselev.enemy.mongo.MongoService;
import com.kiselev.instagram.model.InstagramProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PublicEnemy {

    private final InstagramService instagram;

    private final MongoService mongo;

    private final List<String> usernames;

    //@Scheduled(cron = "0 0 */5 * * ?")
    public void operate() {
        for (String username : usernames) {
            InstagramProfile actualProfile = instagram.read(username);

            InstagramProfile latestProfile = mongo.read(actualProfile.getPk());

            List<String> messages = instagram.compare(actualProfile, latestProfile);

            if (!messages.isEmpty()) {
                write(messages);

                mongo.save(actualProfile);
            }
        }
    }

    private void write(List<String> messages) {
        messages.forEach(System.out::println);
    }
}
