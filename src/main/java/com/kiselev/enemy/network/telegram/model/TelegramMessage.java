package com.kiselev.enemy.network.telegram.model;

import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.Info;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class TelegramMessage<Profile extends Info> {

    private MessageType type;

//    private Analysis<Profile> analysis;
//
//    private List<EnemyMessage<Profile>> messages;
//
//    private EnemyMessage<Profile> message;

    private String text;

//    public static <Profile extends Info> TelegramMessage<Profile> analysis(Analysis<Profile> analysis) {
//        TelegramMessage<Profile> message = new TelegramMessage<>();
//        message.type = MessageType.ANALYSIS;
//        message.analysis = analysis;
//        return message;
//    }
//
//    public static <Profile extends Info> TelegramMessage<Profile> messages(List<EnemyMessage<Profile>> messages) {
//        TelegramMessage<Profile> message = new TelegramMessage<>();
//        message.type = MessageType.MULTI;
//        message.messages = messages;
//        return message;
//    }
//
//    public static <Profile extends Info> TelegramMessage<Profile> message(EnemyMessage<Profile> rawMessage) {
//        TelegramMessage<Profile> message = new TelegramMessage<>();
//        message.type = MessageType.SINGLE;
//        message.message = rawMessage;
//        return message;
//    }

    public static TelegramMessage<?> text(String text) {
        TelegramMessage<?> message = new TelegramMessage<>();
        message.type = MessageType.TEXT;
        message.text = text;
        return message;
    }

    public static TelegramMessage<?> raw(String text) {
        TelegramMessage<?> message = new TelegramMessage<>();
        message.type = MessageType.RAW;
        message.text = text;
        return message;
    }

//    public byte[] data() {
//        return analysis.photo();
//    }
//
//    public List<String> analysis() {
//        return answers(analysis.messages());
//    }
//
//    public List<String> messages() {
//        return answers(messages);
//    }
//
//    public String message() {
//        return answer(message);
//    }

    public String text() {
        return text;
    }

//    private List<String> answers(List<EnemyMessage<Profile>> enemyMessages) {
//        Map<Profile, String> groupedMessages = enemyMessages.stream()
//                .filter(Objects::nonNull)
//                .filter(profileMessage -> StringUtils.isNotEmpty(profileMessage.getMessage()))
//                .collect(Collectors.groupingBy(
//                        EnemyMessage::getProfile,
//                        Collectors.mapping(EnemyMessage::getMessage, Collectors.joining("\n\n"))
//                ));
//
//        return groupedMessages.entrySet().stream()
//                .map(entry -> entry.getKey().header() + entry.getValue())
//                .collect(Collectors.toList());
//    }
//
//    private String answer(EnemyMessage<Profile> enemyMessage) {
//        return enemyMessage.getProfile().header() + enemyMessage.getMessage();
//    }

    public enum MessageType {
        ANALYSIS, MULTI, SINGLE, TEXT, RAW
    }
}
