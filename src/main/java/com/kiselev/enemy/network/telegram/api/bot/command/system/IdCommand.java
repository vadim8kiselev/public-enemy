package com.kiselev.enemy.network.telegram.api.bot.command.system;

import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.service.PublicEnemyService;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCommand implements TelegramCommand {

    private final PublicEnemyService publicEnemy;

    @Override
    public String command() {
        return "/id";
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = update.message().from().id();
        publicEnemy.tg().send(requestId, TelegramMessage.message("Your id is " + requestId));
    }
}
