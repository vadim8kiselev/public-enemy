package com.kiselev.enemy.network.telegram.service.handler.command.system;

import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.service.TelegramService;
import com.kiselev.enemy.network.telegram.service.handler.TelegramCommand;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCommand implements TelegramCommand {

    private final TelegramService telegram;

    @Override
    public String command() {
        return "/id";
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = update.message().from().id();
        telegram.send(requestId, TelegramMessage.message("Your id is " + requestId));
    }
}
