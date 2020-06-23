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

            Map<String, Object> messages = instagram.compare(actualProfile, latestProfile);

            if (!messages.isEmpty()) {
                write(messages);

                mongo.save(actualProfile);
            }
        }
    }

    // TODO: rewrite printing reports
    @SuppressWarnings("unchecked")
    private void write(Map<String, Object> messages) {
        for (Map.Entry<String, Object> message : messages.entrySet()) {
            String key = message.getKey();
            Object value = message.getValue();

            if (value instanceof String) {
                System.out.println(key + " " + value);
            } else if (value instanceof Map) {
                write((Map<String, Object>) value);
            } else {
                if (value != null) {
                    throw new RuntimeException("Something bad is happened, please check me out");
                }
            }
        }
    }
}
