package com.kiselev.enemy.data.telegram.service;

import com.kiselev.enemy.data.telegram.utils.TelegramUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramAPI {

    public void send(TelegramBot bot, Number id, List<String> messages) {
        for (String message : messages) {
            send(bot, id, message);
        }
    }

    public void send(TelegramBot bot, Number id, String message) {
        String escapedMessage = TelegramUtils.escapeMessage(message);

        SendResponse response = bot.execute(
                new SendMessage(id, escapedMessage)
                        .parseMode(ParseMode.MarkdownV2)
        );
        if (!response.isOk()) {
            throw new RuntimeException(
                    String.format("Cannot send a message to recipient with id [%s]\n"
                                    + "Message: %s\n"
                                    + "Reason:  %s",
                            id,
                            message,
                            response.description()
                    )
            );
        }
    }

    public void send(TelegramBot bot, Number id, byte[] photo, List<String> messages) {
        for (String message : messages) {
            send(bot, id, photo, message);
        }
    }

    public void send(TelegramBot bot, Number id, byte[] photo, String message) {
        String escapedMessage = TelegramUtils.escapeMessage(message);

        SendResponse response = bot.execute(
                new SendPhoto(id, photo)
                        .caption(escapedMessage)
                        .parseMode(ParseMode.MarkdownV2)
        );
        if (!response.isOk()) {
            throw new RuntimeException(
                    String.format("Cannot send a message to recipient with id [%s]\n"
                                    + "Message: %s\n"
                                    + "Reason:  %s",
                            id,
                            escapedMessage,
                            response.description()
                    )
            );
        }
    }
}
