package com.kiselev.enemy.network.telegram.api.bot.command.my;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyIgCommand implements TelegramCommand {

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

        if (StringUtils.isNotEmpty(igIdentifier)) {
            Analysis<InstagramProfile> igResponse = publicEnemy.ig().analyze(igIdentifier);
            publicEnemy.tg().send(requestId, TelegramMessage.analysis(igResponse));
        }
    }
}
