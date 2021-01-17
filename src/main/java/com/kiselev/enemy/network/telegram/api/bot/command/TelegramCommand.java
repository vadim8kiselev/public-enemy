package com.kiselev.enemy.network.telegram.api.bot.command;

import com.pengrad.telegrambot.model.Update;

public interface TelegramCommand {

    String command();

    void execute(Update update, String... args);
}
