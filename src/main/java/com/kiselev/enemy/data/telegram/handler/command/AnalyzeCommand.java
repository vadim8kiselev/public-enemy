package com.kiselev.enemy.data.telegram.handler.command;

import com.kiselev.enemy.data.telegram.handler.TelegramCommand;
import com.kiselev.enemy.data.telegram.service.TelegramAPI;
import com.kiselev.enemy.data.telegram.utils.TelegramUtils;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzeCommand implements TelegramCommand {

    private final TelegramAPI service;

    private final PublicEnemyService publicEnemy;

    @Override
    public void execute(TelegramBot bot, Update update, String... args) {
        Integer requestId = update.message().from().id();
        String request = update.message().text();

        String vkId = TelegramUtils.identifier(SocialNetwork.VK, request);
        if (vkId != null) {
            Analysis<VKProfile> vkResponse = publicEnemy.vk().analyze(vkId);
            List<String> answers = TelegramUtils.answers(vkResponse.messages());
            service.send(bot, requestId, vkResponse.photo(), answers);
            return;
        }

        String igId = TelegramUtils.identifier(SocialNetwork.IG, request);
        if (igId != null) {
            Analysis<InstagramProfile> igResponse = publicEnemy.ig().analyze(igId);
            List<String> answers = TelegramUtils.answers(igResponse.messages());
            service.send(bot, requestId, igResponse.photo(), answers);
            return;
        }
    }
}