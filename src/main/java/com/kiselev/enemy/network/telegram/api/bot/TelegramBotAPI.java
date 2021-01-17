package com.kiselev.enemy.network.telegram.api.bot;

import com.kiselev.enemy.network.telegram.api.bot.internal.TelegramBotClient;
import com.kiselev.enemy.network.telegram.utils.TelegramUtils;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotAPI {

    private final TelegramBotClient api;

    public TelegramBotClient raw() {
        return api;
    }

//    public void ask(Integer id, String question) {
//        SendResponse response = api.send(
//                new SendMessage(id, question)
//                        .replyMarkup(new ForceReply())
//                        .parseMode(ParseMode.MarkdownV2)
//                        .disableWebPagePreview(true)
//        );
//        if (!response.isOk()) {
//            throw new RuntimeException(
//                    String.format("Cannot send a message to recipient with id [%s]\n"
//                                    + "Message: %s\n"
//                                    + "Reason:  %s",
//                            id,
//                            question,
//                            response.description()
//                    )
//            );
//        }
//    }

    public void send(Integer id, List<String> messages) {
        for (String message : messages) {
            send(id, message);
        }
    }

    public void sendPhoto(Integer id, byte[] photo, List<String> messages) {
        for (String message : messages) {
            sendPhoto(id, photo, message);
        }
    }

    public void send(Integer id, String message) {
        String escapedMessage = TelegramUtils.escapeMessage(message);

        SendResponse response = api.send(
                new SendMessage(id, escapedMessage)
                        .parseMode(ParseMode.MarkdownV2)
                        .disableWebPagePreview(true)
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

    public void sendRaw(Integer id, String message) {
        SendResponse response = api.send(
                new SendMessage(id, message)
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

    public void sendPhoto(Integer id, byte[] photo, String message) {
        String escapedMessage = TelegramUtils.escapeMessage(message);

        SendResponse response = api.send(
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
