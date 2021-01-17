package com.kiselev.enemy.network.telegram.api.bot;

import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_NONE;

@Service
@RequiredArgsConstructor
public class TelegramHandler {

    private final TelegramBotAPI api;

    private final List<TelegramCommand> commands;

    @Value("${com.kiselev.enemy.telegram.id:}")
    private Number me;

    @Value("${com.kiselev.enemy.telegram.bot.enabled}")
    private Boolean enabled;

    @Value("${com.kiselev.enemy.telegram.bot.timeout}")
    private Long timeout;

    @PostConstruct
    public void initialize() {
        if (enabled) {
            poll(
                    updates -> {
                        for (Update update : updates) {
                            handle(update);
                        }
                        return CONFIRMED_UPDATES_ALL;
                    },
                    new GetUpdates());
        }
    }

    public void handle(Update update) {
        Integer requestId = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::from)
                .map(User::id)
                .orElse(null);

        String request = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::text)
                .orElse(null);

        if (requestId != null && request != null) {
            TelegramCommand command = recognizeCommand(request);

            if (command != null) {
                try {
                    command.execute(update);
                } catch (Exception exception) {
                    api.send(requestId, String.format("Id: \"%s\", Text: \"%s\", Error:\n%s", requestId, request, exception.getMessage()));
                    if (ObjectUtils.notEqual(requestId, me)) {
                        api.send(me, String.format("Id: \"%s\", Text: \"%s\", Error:\n%s", requestId, request, exception.getMessage()));
                    }
                }
            } else {
                api.send(requestId, String.format("Unknown type of command: \"%s\"", request));
            }
        } else {
            api.send(me, String.format("Illegal arguments\\: id\\=\"%s\", text\\=\"%s\"", requestId, request));
        }
    }

    private TelegramCommand recognizeCommand(String text) {
        return commands.stream()
                .filter(command -> text.startsWith(command.command()))
                .findFirst()
                .orElse(null);
    }

    private void poll(UpdatesListener listener, GetUpdates request) {
        api.raw().send(request, new Callback<GetUpdates, GetUpdatesResponse>() {
            @Override
            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                if (!response.isOk() || response.updates() == null || response.updates().size() <= 0) {
                    if (!response.isOk()) {
                        Logger.getGlobal().log(Level.INFO,
                                "Update listener error for request " + request.toWebhookResponse() +
                                        " with response " + response.errorCode() + " " + response.description());
                    }
                    sleep();
                    poll(listener, request);
                    return;
                }

                List<Update> updates = response.updates();
                int lastConfirmedUpdate = listener.process(updates);

                if (lastConfirmedUpdate != CONFIRMED_UPDATES_NONE) {
                    int offset = lastConfirmedUpdate == CONFIRMED_UPDATES_ALL
                            ? lastUpdateId(updates) + 1
                            : lastConfirmedUpdate + 1;
                    request = request.offset(offset);
                }
                poll(listener, request);
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {
                Logger.getGlobal().log(Level.INFO, "Update listener failure", e);
                sleep();
                poll(listener, request);
            }
        });
    }

    private int lastUpdateId(List<Update> updates) {
        return updates.get(updates.size() - 1).updateId();
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(timeout);
    }
}
