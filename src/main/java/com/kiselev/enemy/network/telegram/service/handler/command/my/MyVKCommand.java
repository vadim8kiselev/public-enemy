package com.kiselev.enemy.network.telegram.service.handler.command.my;

import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.service.TelegramService;
import com.kiselev.enemy.network.telegram.service.handler.TelegramCommand;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyVKCommand implements TelegramCommand {

    private final TelegramService telegram;

    private final PublicEnemyService publicEnemy;

    @Value("${com.kiselev.enemy.vk.identifier:}")
    private String vkIdentifier;

    @Override
    public String command() {
        return "/vk";
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = update.message().from().id();

        if (vkIdentifier != null) {
            Analysis<VKProfile> vkResponse = publicEnemy.vk().analyze(vkIdentifier);
            telegram.send(requestId, TelegramMessage.analysis(vkResponse));
        }
    }
}