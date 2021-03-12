package com.kiselev.enemy.network.telegram;

import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.service.TelegramService;
import com.kiselev.enemy.utils.flow.model.Info;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
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

    public TelegramService service() {
        return telegram;
    }

    public TelegramProfile me() {
        return telegram.me();
    }

    public TelegramProfile profile(String id) {
        return telegram.profile(id);
    }

    public <Profile extends Info> SendResponse send(TelegramMessage<Profile> message) {
        return telegram.send(me, message);
    }

    public <Profile extends Info> SendResponse send(Integer id, TelegramMessage<Profile> message) {
        if (ObjectUtils.notEqual(me, id)) {
            telegram.send(me, message);
        }
        return telegram.send(id, message);
    }

    public <Profile extends Info> BaseResponse update(Integer messageId, TelegramMessage<Profile> message) {
        return telegram.update(me, messageId, message);
    }

    public <Profile extends Info> BaseResponse update(Integer id, Integer messageId, TelegramMessage<Profile> message) {
        return telegram.update(id, messageId, message);
    }

    @SneakyThrows
    public Map<TelegramProfile, Set<TLMessage>> history() {
        return telegram.history();
    }
}
