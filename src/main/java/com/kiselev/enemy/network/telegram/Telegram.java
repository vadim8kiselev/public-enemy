package com.kiselev.enemy.network.telegram;

import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.service.TelegramService;
import com.kiselev.enemy.utils.flow.model.Info;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.api.message.TLMessage;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Telegram {

    private final TelegramService telegram;

    public TelegramProfile me() {
        return telegram.me();
    }

    public <Profile extends Info> void send(TelegramMessage<Profile> message) {
        telegram.send(message);
    }

    public <Profile extends Info> void send(Number id, TelegramMessage<Profile> message) {
        telegram.send(id, message);
    }

    @SneakyThrows
    public Map<TelegramProfile, Set<TLMessage>> history() {
        return telegram.history();
    }
}
