package com.kiselev.enemy.network.telegram.service.handler;

import com.pengrad.telegrambot.model.Update;

public interface TelegramCommand {

    String command();

    void execute(Update update, String... args);
}
