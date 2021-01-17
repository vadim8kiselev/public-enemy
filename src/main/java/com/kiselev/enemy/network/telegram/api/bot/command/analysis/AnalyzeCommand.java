package com.kiselev.enemy.network.telegram.api.bot.command.analysis;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeCommand implements TelegramCommand {

    private final PublicEnemyService publicEnemy;

    @Override
    public String command() {
        return "/analyze";
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = update.message().from().id();
        String request = update.message().text();

        String vkId = ProfilingUtils.identifier(SocialNetwork.VK, request);
        if (vkId != null) {
            Analysis<VKProfile> vkResponse = publicEnemy.vk().analyze(vkId);
            publicEnemy.tg().send(requestId, TelegramMessage.analysis(vkResponse));
            return;
        }

        String igId = ProfilingUtils.identifier(SocialNetwork.IG, request);
        if (igId != null) {
            Analysis<InstagramProfile> igResponse = publicEnemy.ig().analyze(igId);
            publicEnemy.tg().send(requestId, TelegramMessage.analysis(igResponse));
            return;
        }
    }
}
