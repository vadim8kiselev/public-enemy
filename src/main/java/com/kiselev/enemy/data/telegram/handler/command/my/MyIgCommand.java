package com.kiselev.enemy.data.telegram.handler.command.my;

import com.kiselev.enemy.data.telegram.handler.TelegramCommand;
import com.kiselev.enemy.data.telegram.service.TelegramAPI;
import com.kiselev.enemy.data.telegram.utils.TelegramUtils;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyIgCommand implements TelegramCommand {

    private final TelegramAPI service;

    private final PublicEnemyService publicEnemy;

    @Value("${com.kiselev.enemy.instagram.identifier:}")
    private String igIdentifier;

    @Override
    public void execute(TelegramBot bot, Update update, String... args) {
        Integer requestId = update.message().from().id();

        if (igIdentifier != null) {
            Analysis<InstagramProfile> igResponse = publicEnemy.ig().analyze(igIdentifier);
            List<String> answers = TelegramUtils.answers(igResponse.messages());
            service.send(bot, requestId, igResponse.photo(), answers);
        }
    }
}
