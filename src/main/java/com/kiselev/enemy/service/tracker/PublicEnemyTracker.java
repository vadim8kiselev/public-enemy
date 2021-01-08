package com.kiselev.enemy.service.tracker;

import com.kiselev.enemy.data.telegram.Telegram;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
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
public class PublicEnemyTracker {

    private final VK vk;

    private final Instagram instagram;

    private final Telegram telegram;

    @Value("${com.kiselev.enemy.vk.enabled}")
    private Boolean vkIsEnabled;

    @Value("${com.kiselev.enemy.vk.identifier:}")
    private String vkIdentifier;

    @Value("${com.kiselev.enemy.instagram.enabled}")
    private Boolean igIsEnabled;

    @Value("${com.kiselev.enemy.instagram.identifier:}")
    private String igIdentifier;

    @SneakyThrows
    @Scheduled(cron = "0 0/1 * * * *")
    public void vk() {
        if (vkIsEnabled) {
            List<EnemyMessage<VKProfile>> profile = vk.track(vkIdentifier);
            telegram.send(profile);
        }
    }

    @SneakyThrows
    @Scheduled(cron = "0 0/1 * * * *")
    public void ig() {
        if (igIsEnabled) {
            List<EnemyMessage<InstagramProfile>> profile = instagram.track(igIdentifier);
            telegram.send(profile);
        }
    }
}
