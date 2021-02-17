package com.kiselev.enemy.network.telegram.api.bot;

import com.kiselev.enemy.network.telegram.api.bot.internal.TelegramBotClient;
import com.kiselev.enemy.network.telegram.utils.TelegramUtils;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.VideoNote;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotAPI {

    private final TelegramBotClient api;

    public TelegramBotClient raw() {
        return api;
    }

    public void ask(Integer id, String question) {
        SendResponse response = api.send(
                new SendMessage(id, question)
                        .replyMarkup(new ForceReply())
                        .parseMode(ParseMode.MarkdownV2)
                        .disableWebPagePreview(true)
        );
        processResponse(response, id, question);
    }

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
        processResponse(response, id, message);
    }

    public void sendRaw(Integer id, String message) {
        SendResponse response = api.send(
                new SendMessage(id, message)
                        .disableWebPagePreview(true)
        );
        processResponse(response, id, message);
    }

    public void sendPhoto(Integer id, byte[] photo, String message) {
        String escapedMessage = TelegramUtils.escapeMessage(message);

        SendResponse response = api.send(
                new SendPhoto(id, photo)
                        .caption(escapedMessage)
                        .parseMode(ParseMode.MarkdownV2)
        );
        processResponse(response, id, message);
    }

    private void processResponse(BaseResponse response,
                                 Integer id,
                                 String body) {
        if (!response.isOk()) {
            throw new RuntimeException(
                    String.format("Cannot send a message to recipient with id [%s]\n"
                                    + "Message: %s\n"
                                    + "Reason:  %s",
                            id,
                            body,
                            response.description()
                    )
            );
        }
    }

    public void sendPhoto(Integer id, byte[] photo) {
        SendResponse response = api.send(
                new SendPhoto(id, photo)
        );
        processResponse(response, id, "Photo file");
    }

    public void sendVideo(Integer id, byte[] video) {
        SendResponse response = api.send(
                new SendVideo(id, video)
        );
        processResponse(response, id, "Video file");
    }

    public void sendVideoNote(Integer id, byte[] videoNote) {
        SendResponse response = api.send(
                new SendVideoNote(id, videoNote)
        );
        processResponse(response, id, "Video note file");
    }

    public byte[] download(String fileId) {
        GetFileResponse response = api.send(
                new GetFile(fileId)
        );

        File file = response.file();
        return api.download(file.filePath());
    }

//    public void sendMenu(Integer id, String message) {
//        SendResponse response = api.send(
//                new SendMessage(id, message)
//                        .replyMarkup(new InlineKeyboardMarkup(
//                                new InlineKeyboardButton("Button 1").url("vk.com/kiselev"),
//                                new InlineKeyboardButton("Button 2").url("vk.com/kiselev"),
//                                new InlineKeyboardButton("Button 3").url("vk.com/kiselev")
//                        ))
//        );
//        processResponse(response, id, message);
//    }

    public void sendTyping(Integer id) {
        BaseResponse response = api.send(
                new SendChatAction(id, ChatAction.typing)
        );
        processResponse(response, id, response.description());
    }
}
