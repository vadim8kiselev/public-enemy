package com.kiselev.enemy.data.telegram.utils;

import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.Info;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TelegramUtils {

    public static String escapeMessage(String message) {
        return message
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\_", "\\\\_")
//                .replaceAll("\\[", "\\\\[")
//                .replaceAll("\\]", "\\\\]")
//                .replaceAll("\\(", "\\\\(")
//                .replaceAll("\\)", "\\\\)")
                .replaceAll("\\-", "\\\\-");
    }

    public static String identifier(SocialNetwork socialNetwork, String request) {
        Pattern pattern = socialNetwork.pattern();
        Matcher matcher = pattern.matcher(request);

        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }

    public static <Profile extends Info> List<String> answers(List<EnemyMessage<Profile>> messages) {
        Map<Profile, String> groupedMessages = messages.stream()
                .filter(Objects::nonNull)
                .filter(profileMessage -> StringUtils.isNotEmpty(profileMessage.getMessage()))
                .collect(Collectors.groupingBy(
                        EnemyMessage::getProfile,
                        Collectors.mapping(EnemyMessage::getMessage, Collectors.joining("\n\n"))
                ));

        return groupedMessages.entrySet().stream()
                .map(entry -> entry.getKey().header() + entry.getValue())
                .collect(Collectors.toList());
    }
}
