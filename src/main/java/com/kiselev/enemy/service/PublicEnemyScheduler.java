package com.kiselev.enemy.service;

import com.kiselev.enemy.data.telegram.Telegram;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.network.instagram.Instagram;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.vk.VK;
import com.kiselev.enemy.network.vk.model.VKProfile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicEnemyScheduler {

    private final VK vk;

    private final Instagram instagram;

    private final Telegram telegram;

    @Value("${com.kiselev.enemy.vk.enabled}")
    private Boolean vkIsEnabled;

    @Value("${com.kiselev.enemy.vk.identifiers}")
    private List<String> vkIdentifiers;

    @Value("${com.kiselev.enemy.instagram.enabled}")
    private Boolean igIsEnabled;

    @Value("${com.kiselev.enemy.instagram.identifiers}")
    private List<String> igIdentifiers;

    @SneakyThrows
    @Scheduled(cron = "0 0/1 * * * *")
    public void vk() {
        if (vkIsEnabled) {
            for (String id : vkIdentifiers) {
                List<Message<VKProfile>> profile = vk.track(id);
                telegram.send(profile);
            }
        }
    }

    @SneakyThrows
    @Scheduled(cron = "0 0/1 * * * *")
    public void ig() {
        if (igIsEnabled) {
            for (String username : igIdentifiers) {
                List<Message<InstagramProfile>> profile = instagram.track(username);
                telegram.send(profile);
            }
        }
    }
}
