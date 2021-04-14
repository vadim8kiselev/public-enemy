package com.kiselev.enemy.network.telegram.service;

import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.utils.flow.model.Info;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Data
@RequiredArgsConstructor
public class TelegramDialogue {

    private final TelegramService telegram;

    private final Integer me;

    private Integer myMessageId;

    public <Profile extends Info> SendResponse send(TelegramMessage<Profile> message) {
        return telegram.send(me, message);
    }

    public <Profile extends Info> SendResponse send(Integer id, TelegramMessage<Profile> message) {
        if (ObjectUtils.notEqual(me, id)) {
            if (myMessageId == null) {
                SendResponse myResponse = telegram.send(me, message);
                this.myMessageId = myResponse.message().messageId();
            } else {
                this.myMessageId = null;
                throw new RuntimeException("Message id is already present. Contact administrator");
            }
        }
        return telegram.send(id, message);
    }

    public <Profile extends Info> BaseResponse update(Integer messageId, TelegramMessage<Profile> message) {
        return telegram.update(me, messageId, message);
    }

    public <Profile extends Info> BaseResponse update(Integer id, Integer messageId, TelegramMessage<Profile> message) {
        if (ObjectUtils.notEqual(me, id)) {
            if (myMessageId != null) {
                telegram.update(me, myMessageId, message);
            } else {
                throw new RuntimeException("Message id is absent. Contact administrator");
            }
        }
        return telegram.update(id, messageId, message);
    }
}
