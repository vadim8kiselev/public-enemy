package com.kiselev.enemy.network.telegram.api.bot.command.profiling;

import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.PublicEnemyProfiler;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.utils.flow.model.Id;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileCommand implements TelegramCommand {

    private final PublicEnemyService publicEnemy;

    private final PublicEnemyProfiler publicEnemyProfiler;

    @Override
    public String command() {
        return "/profile";
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::from)
                .map(User::id)
                .orElse(null);

        String request = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::text)
                .orElse(null);

        Person person = publicEnemyProfiler.profile(request);

        publicEnemy.tg().send(requestId, TelegramMessage.message(
                String.format("Name: %s, Instagram: %s, Telegram: %s, VK: %s",
                        person.getFullName(),
                        person.getInstagram(),
                        person.getTelegram(),
                        person.getVk())
        ));
    }
}
