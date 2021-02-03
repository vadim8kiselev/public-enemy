package com.kiselev.enemy.network.telegram.api.bot.command.query;

import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class QueryCommand implements TelegramCommand {

    private final PublicEnemyService publicEnemy;

    @Override
    public boolean is(Update update) {
        return false;
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

        // How much(many) ......... do(es) . have(s)?
        // -------- followers ------ I -------?

        // Show/Give/List my followers

        // What is the average age among my followers?

        if (request != null) {
            Pattern pattern = Pattern.compile("[Hh]ow (much|many) (\\w+) (do|does) (\\w+) (have|has)\\?");
            Matcher matcher = pattern.matcher(request);
            if (matcher.find()) {
                String a = matcher.group(2);
                String b = matcher.group(4);
            }
        }
    }
}
