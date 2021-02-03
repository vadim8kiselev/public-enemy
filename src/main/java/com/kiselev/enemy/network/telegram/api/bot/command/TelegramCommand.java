package com.kiselev.enemy.network.telegram.api.bot.command;

import com.pengrad.telegrambot.model.Update;

public interface TelegramCommand {

    boolean is(Update update);

    void execute(Update update, String... args);
}
