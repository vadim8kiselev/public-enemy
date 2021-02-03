package com.kiselev.enemy.service.tracker;

import com.kiselev.enemy.network.instagram.Instagram;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.Telegram;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.vk.VK;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
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

    @Value("${com.kiselev.enemy.telegram.id}")
    private Integer telegramIdentifier;

//    @SneakyThrows
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void vk() {
//        if (vkIsEnabled) {
//            List<EnemyMessage<VKProfile>> profile = vk.track(vkIdentifier);
//            telegram.send(telegramIdentifier, TelegramMessage.messages(profile));
//        }
//    }
//
//    @SneakyThrows
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void ig() {
//        if (igIsEnabled) {
//            List<EnemyMessage<InstagramProfile>> profile = instagram.track(igIdentifier);
//            telegram.send(telegramIdentifier, TelegramMessage.messages(profile));
//        }
//    }
}
