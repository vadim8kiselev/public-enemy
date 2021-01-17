package com.kiselev.enemy.network.telegram.api;

import com.kiselev.enemy.network.telegram.api.bot.TelegramBotAPI;
import com.kiselev.enemy.network.telegram.api.client.TelegramClientAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramAPI {

    private final TelegramBotAPI botAPI;

    private final TelegramClientAPI clientAPI;

    public TelegramBotAPI bot() {
        return botAPI;
    }

    public TelegramClientAPI client() {
        return clientAPI;
    }
}
