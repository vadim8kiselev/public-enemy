package com.kiselev.enemy.utils.flow.utils;

import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.model.Id;
import com.kiselev.enemy.utils.flow.model.Info;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlowUtils {

    public static <Profile extends Info> Message<Profile> attribute(Function<Profile, String> function,
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

    public static <Profile extends Info> Message<Profile> attribute(Profile actual,
                                                                    String actualValue,
                                                                    String latestValue,
                                                                    String message) {
        if (!Objects.equals(actualValue, latestValue)) {
            return Message.of(
                    actual,
                    String.format(message, latestValue, actualValue)
            );
        }
        return null;
    }

    public static <Profile extends Info, Type extends Id> List<Message<Profile>> attributes(Function<Profile, List<Type>> function,
                                                                                            Profile actual,
                                                                                            Profile latest,
                                                                                            String message) {
        List<Type> actualValues = function.apply(actual);
        List<Type> latestValues = function.apply(latest);
        Map<String, Type> actualMap = actualValues.stream()
                .collect(Collectors.toMap(
                        Type::id,
                        post -> post
                ));

        Map<String, Type> latestMap = latestValues.stream()
                .collect(Collectors.toMap(
                        Type::id,
                        post -> post
                ));

        Set<String> actualIds = actualMap.keySet();
        Set<String> latestIds = latestMap.keySet();

        actualIds.removeAll(latestIds);

        Set<Type> newItems = actualIds.stream()
                .map(actualMap::get)
                .collect(Collectors.toSet());

        return newItems.stream()
                .map(item -> Message.of(actual, String.format(message, item.name())))
                .collect(Collectors.toList());
    }
}