package com.kiselev.enemy.data.telegram;

import com.kiselev.enemy.data.telegram.handler.TelegramHandler;
import com.kiselev.enemy.data.telegram.service.TelegramAPI;
import com.kiselev.enemy.data.telegram.utils.TelegramUtils;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.model.Info;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Telegram {

    private final TelegramHandler handler;

    private final TelegramAPI service;

    @Value("${com.kiselev.enemy.telegram.enabled}")
    private Boolean enabled;

    @Value("${com.kiselev.enemy.telegram.token}")
    private String token;

    @Value("${com.kiselev.enemy.telegram.id}")
    private Long id;

    private TelegramBot bot;

    @PostConstruct
    public void initialize() {
        this.bot = new TelegramBot(token);

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handler.handle(bot, update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public <Profile extends Info> void send(Long requestId, Analysis<Profile> analysis) {
        if (enabled) {
            List<String> answers = TelegramUtils.answers(analysis.messages());
            service.send(bot, requestId, analysis.photo(), answers);
            service.send(bot, id, analysis.photo(), answers);
        }
    }

    public <Profile extends Info> void send(Analysis<Profile> analysis) {
        if (enabled) {
            List<String> answers = TelegramUtils.answers(analysis.messages());
            service.send(bot, id, analysis.photo(), answers);
        }
    }

    public <Profile extends Info> void send(Long requestId, List<Message<Profile>> messages) {
        if (enabled) {
            List<String> answers = TelegramUtils.answers(messages);
            service.send(bot, requestId, answers);
            service.send(bot, id, answers);
        }
    }

    public <Profile extends Info> void send(List<Message<Profile>> messages) {
        if (enabled) {
            List<String> answers = TelegramUtils.answers(messages);
            service.send(bot, id, answers);
        }
    }
}
