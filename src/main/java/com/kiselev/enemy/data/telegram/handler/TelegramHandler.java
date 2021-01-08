package com.kiselev.enemy.data.telegram.handler;

import com.kiselev.enemy.data.telegram.handler.command.AnalyzeCommand;
import com.kiselev.enemy.data.telegram.handler.command.InfoCommand;
import com.kiselev.enemy.data.telegram.handler.command.my.MyIgCommand;
import com.kiselev.enemy.data.telegram.handler.command.my.MyVKCommand;
import com.kiselev.enemy.data.telegram.service.TelegramAPI;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramHandler {

    private final TelegramAPI service;

    private final MyVKCommand myVKCommand;

    private final MyIgCommand myIgCommand;

    private final AnalyzeCommand analyzeCommand;

    private final InfoCommand infoCommand;

    public void handle(TelegramBot bot, Update update) {
        Integer requestId = update.message().from().id();
        String request = update.message().text();

        TelegramCommand command = recognizeCommand(request);

        if (command != null) {
            command.execute(bot, update);
        } else {
            service.send(bot, requestId, String.format("Unknown type of command: \"%s\"", request));
        }
    }

    private TelegramCommand recognizeCommand(String text) {
        if (text.startsWith("/vk")) {
            return myVKCommand;
        }

        if (text.startsWith("/ig") || text.startsWith("/instagram")) {
            return myIgCommand;
        }

        if (text.startsWith("/analyze")) {
            return analyzeCommand;
        }

        if (text.startsWith("/info")) {
            return infoCommand;
        }

        return null;
    }
}
