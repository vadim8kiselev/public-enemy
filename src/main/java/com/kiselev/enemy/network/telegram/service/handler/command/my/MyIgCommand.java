package com.kiselev.enemy.network.telegram.service.handler.command.my;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.service.TelegramService;
import com.kiselev.enemy.network.telegram.service.handler.TelegramCommand;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyIgCommand implements TelegramCommand {

    private final TelegramService telegram;

    private final PublicEnemyService publicEnemy;

    @Value("${com.kiselev.enemy.instagram.identifier:}")
    private String igIdentifier;

    @Override
    public String command() {
        return "/ig";
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = update.message().from().id();

        if (igIdentifier != null) {
            Analysis<InstagramProfile> igResponse = publicEnemy.ig().analyze(igIdentifier);
            telegram.send(requestId, TelegramMessage.analysis(igResponse));
        }
    }
}