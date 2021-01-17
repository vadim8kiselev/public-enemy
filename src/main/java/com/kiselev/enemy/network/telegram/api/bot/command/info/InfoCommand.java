package com.kiselev.enemy.network.telegram.api.bot.command.info;

import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.service.PublicEnemyService;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Integer requestId = update.message().from().id();
        String request = update.message().text();

//        String vkId = TelegramUtils.identifier(SocialNetwork.VK, request);
//        if (vkId != null) {
//            Analysis<VKProfile> vkResponse = publicEnemy.vk().analyze(vkId);
//            List<String> answers = TelegramUtils.answers(vkResponse.messages());
//            service.send(bot, requestId, vkResponse.photo(), answers);
//            return;
//        }
//
//        String igId = TelegramUtils.identifier(SocialNetwork.IG, request);
//        if (igId != null) {
//            Analysis<InstagramProfile> igResponse = publicEnemy.ig().analyze(igId);
//            List<String> answers = TelegramUtils.answers(igResponse.messages());
//            service.send(bot, requestId, igResponse.photo(), answers);
//            return;
//        }

        publicEnemy.tg().send(requestId, TelegramMessage.message("This command is currently not supported"));
    }
}
