package com.kiselev.enemy.network.telegram.api.bot.command.info;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoCommand implements TelegramCommand {

    private final PublicEnemyService publicEnemy;

    @Override
    public String command() {
        return "/info";
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

        String vkId = ProfilingUtils.identifier(SocialNetwork.VK, request);
        if (vkId != null) {
            List<EnemyMessage<VKProfile>> info = publicEnemy.vk().info(vkId);
            publicEnemy.tg().send(requestId, TelegramMessage.messages(info));
            return;
        }

        String igId = ProfilingUtils.identifier(SocialNetwork.IG, request);
        if (igId != null) {
            List<EnemyMessage<InstagramProfile>> info = publicEnemy.ig().info(igId);
            publicEnemy.tg().send(requestId, TelegramMessage.messages(info));
            return;
        }

        publicEnemy.tg().send(requestId, TelegramMessage.message(String.format("Request %s is not recognized", request)));
    }
}
