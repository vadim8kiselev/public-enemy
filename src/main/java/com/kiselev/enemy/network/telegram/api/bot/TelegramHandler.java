package com.kiselev.enemy.network.telegram.api.bot;

import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.utils.TelegramUtils;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.VideoNote;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_NONE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramHandler {

    private final TelegramBotAPI api;

    private final List<TelegramCommand> commands;

    @Value("${com.kiselev.enemy.telegram.id:}")
    private Integer me;

    @Value("${com.kiselev.enemy.telegram.bot.enabled}")
    private Boolean enabled;

    @Value("${com.kiselev.enemy.telegram.bot.timeout}")
    private Long timeout;

    @PostConstruct
    public void initialize() {
        if (enabled) {
            UpdatesListener listener = updates -> {
                for (Update update : updates) {
                    handle(update);
                }
                return CONFIRMED_UPDATES_ALL;
            };

            poll(listener, new GetUpdates());
        }
    }

    public void handle(Update update) {
        Message message = Optional.ofNullable(update)
                .map(Update::message)
                .orElse(null);

        if (message != null) {
            Integer requestId = Optional.of(message)
                    .map(Message::from)
                    .map(User::id)
                    .orElse(null);

            String request = Optional.of(message)
                    .map(Message::text)
                    .orElse(null);

            if (requestId != null && request != null) {
                TelegramCommand command = recognizeCommand(update);

                if (command != null) {
                    try {
                        command.execute(update);
                    } catch (Exception exception) {
                        String exceptionMessage = exception.getMessage();
                        String truncatedExceptionMessage = TelegramUtils.truncate(exceptionMessage);
                        String text = String.format("Id: \"%s\", Text: \"%s\", Error:\n%s",
                                requestId, request, truncatedExceptionMessage);

                        log.warn(exceptionMessage, exception);
                        api.sendRaw(requestId, text);
                        if (ObjectUtils.notEqual(requestId, me)) {
                            api.sendRaw(me, text);
                        }
                    }
                } else {
                    api.sendRaw(requestId, String.format("Unknown command: \"%s\"", request));
                }
            } else {
                api.sendRaw(requestId, String.format("Unknown command: \"%s\"", request));
            }
        }
    }

    private TelegramCommand recognizeCommand(Update update) {
        List<TelegramCommand> commands = this.commands.stream()
                .filter(command -> command.is(update))
                .collect(Collectors.toList());

        if (commands.size() != 1) {
            throw new RuntimeException("Error happened while command recognizing process");
        }

        return commands.stream()
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
