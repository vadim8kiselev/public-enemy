package com.kiselev.enemy.data.telegram.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public interface TelegramCommand {

    void execute(TelegramBot bot, Update update, String... args);
}
