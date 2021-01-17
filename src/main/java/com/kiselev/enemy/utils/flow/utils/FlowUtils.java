package com.kiselev.enemy.utils.flow.utils;

import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.Id;
import com.kiselev.enemy.utils.flow.model.Info;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlowUtils {

    public static <Profile extends Info> EnemyMessage<Profile> attribute(Function<Profile, String> function,
                                                                         Profile actual,
                                                                         Profile latest,
                                                                         String message) {
        String actualValue = function.apply(actual);
        String latestValue = function.apply(latest);
        return attribute(actual,
                actualValue,
                latestValue,
                message);
    }

    public static <Profile extends Info> EnemyMessage<Profile> attribute(Profile actual,
                                                                         String actualValue,
                                                                         String latestValue,
                                                                         String message) {
        if (!Objects.equals(actualValue, latestValue)) {
            return EnemyMessage.of(
                    actual,
                    String.format(message, latestValue, actualValue)
            );
        }
        return null;
    }

    public static <Profile extends Info, Type extends Id> List<EnemyMessage<Profile>> attributes(Function<Profile, List<Type>> function,
                                                                                                 Profile actual,
                                                                                                 Profile latest,
                                                                                                 String message) {
        List<Type> actualValues = function.apply(actual);
        List<Type> latestValues = function.apply(latest);
        Map<String, Type> actualMap = actualValues.stream()
                .collect(Collectors.toMap(
                        Type::id,
                        post -> post,
                        (first, second) -> second
                ));

        Map<String, Type> latestMap = latestValues.stream()
                .collect(Collectors.toMap(
                        Type::id,
                        post -> post,
                        (first, second) -> second
                ));

        Set<String> actualIds = actualMap.keySet();
        Set<String> latestIds = latestMap.keySet();

        actualIds.removeAll(latestIds);

        Set<Type> newItems = actualIds.stream()
                .map(actualMap::get)
                .collect(Collectors.toSet());

        return newItems.stream()
                .map(item -> EnemyMessage.of(actual, String.format(message, item.name())))
                .collect(Collectors.toList());
    }
}
