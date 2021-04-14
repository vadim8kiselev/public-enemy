package com.kiselev.enemy.network.telegram.service;

import com.kiselev.enemy.network.telegram.api.TelegramAPI;
import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.utils.flow.model.Info;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.telegram.api.message.TLMessage;
import org.telegram.api.user.TLUser;
import org.telegram.api.user.TLUserFull;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramAPI api;

    public TelegramAPI api() {
        return api;
    }

    public TelegramProfile me() {
        TLUserFull me = api.client().me();

        return new TelegramProfile(me);
    }

    public TelegramProfile profile(String id) {
        if (id == null) {
            return null;
        }

        TLUserFull profile = api.client().profile(id);

        return new TelegramProfile(profile);
    }

    public Map<TelegramProfile, Set<TLMessage>> history() {
        Map<TLUser, Set<TLMessage>> history = api.client().history();

        return history.entrySet().stream()
                .map(entry -> ImmutablePair.of(
                        new TelegramProfile(entry.getKey()),
                        entry.getValue()
                ))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (first, second) -> {
                        if (first.size() != second.size()) {
                            throw new RuntimeException("An error happened while merging");
                        }
                        return second;
                    }
                ));
    }

    public <Profile extends Info> SendResponse send(Integer id, TelegramMessage<Profile> message) {
        TelegramMessage.MessageType type = message.getType();

        switch (type) {
//            case ANALYSIS:
//                api.bot().sendPhoto(id, message.data(), message.analysis());
//                return;
//            case MULTI:
//                api.bot().send(id, message.messages());
//                return;
//            case SINGLE:
//                api.bot().send(id, message.message());
//                return;
            case TEXT:
                return api.bot().send(id, message.text());
            case RAW:
                return api.bot().sendRaw(id, message.text());
            default:
                throw new RuntimeException("Unknown type of the message");
        }
    }

    public <Profile extends Info> BaseResponse update(Integer id, Integer messageId, TelegramMessage<Profile> message) {
        TelegramMessage.MessageType type = message.getType();

        switch (type) {
            case TEXT:
                return api.bot().update(id, messageId, message.text());
            case RAW:
                return api.bot().updateRaw(id, messageId, message.text());
            default:
                throw new RuntimeException("Unknown type of the message");
        }
    }

//    public <Profile extends Info> void send(Long requestId, Analysis<Profile> analysis) {
//            List<String> answers = TelegramUtils.answers(analysis.messages());
//            api.bot().send(requestId, analysis.photo(), answers);
//            api.bot().send(id, analysis.photo(), answers);
//    }
//
//    public <Profile extends Info> void send(Analysis<Profile> analysis) {
//            List<String> answers = TelegramUtils.answers(analysis.messages());
//            api.bot().send(id, analysis.photo(), answers);
//    }
//
//    public <Profile extends Info> void send(Long requestId, List<EnemyMessage<Profile>> messages) {
//            List<String> answers = TelegramUtils.answers(messages);
//            api.bot().send(requestId, answers);
//            api.bot().send(id, answers);
//    }
//
//    public <Profile extends Info> void send(List<EnemyMessage<Profile>> messages) {
//            List<String> answers = TelegramUtils.answers(messages);
//            api.bot().send(id, answers);
//    }
}
