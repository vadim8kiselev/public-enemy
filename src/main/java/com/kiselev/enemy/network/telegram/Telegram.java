package com.kiselev.enemy.network.telegram;

import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.service.TelegramService;
import com.kiselev.enemy.utils.flow.model.Info;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.api.message.TLMessage;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Telegram {

    private final TelegramService telegram;

    @Value("${com.kiselev.enemy.telegram.id:}")
    private Integer me;

    public TelegramProfile me() {
        return telegram.me();
    }

    public TelegramProfile profile(String id) {
        return telegram.profile(id);
    }

    public <Profile extends Info> void send(TelegramMessage<Profile> message) {
        telegram.send(me, message);
    }

    public <Profile extends Info> void send(Integer id, TelegramMessage<Profile> message) {
        telegram.send(id, message);
        if (ObjectUtils.notEqual(me, id)) {
            telegram.send(me, message);
        }
    }

    @SneakyThrows
    public Map<TelegramProfile, Set<TLMessage>> history() {
        return telegram.history();
    }
}
