package com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IdentifierUtils {

    public static <A, B> String prediction(String title,
                                           Function<A, B> function,
                                           List<A> predictionGroup) {
        Prediction<B> prediction = AnalyticsUtils.predict(function, predictionGroup);
        if (prediction != null) {
            return message(title, prediction.message());
        }
        return null;
    }

    public static <A, B> String predictions(String title,
                                            Function<A, List<B>> function,
                                            List<A> predictionGroup) {
        List<B> predictionCandidates = predictionGroup.stream()
                .map(function)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Prediction<B> prediction = AnalyticsUtils.predict(predictionCandidates);
        if (prediction != null) {
            return message(title, prediction.message());
        }
        return null;
    }

    public static String message(String title, Object field) {
        if (field != null) {
            String string = field.toString();
            if (StringUtils.isNotEmpty(string)) {
                return title + ": " + field.toString();
            }
        }
        return null;
    }

    public static String link(String title, SocialNetwork site, Object field) {
        if (field != null) {
            String string = field.toString();
            if (StringUtils.isNotEmpty(string)) {
                return title + ": " + site.link(field.toString(), field.toString());
            }
        }
        return null;
    }

    public static String link(String title, SocialNetwork site, Object name, Object field) {
        if (field != null) {
            String string = field.toString();
            if (StringUtils.isNotEmpty(string)) {
                if (name == null) {
                    name = field;
                }
                return title + ": " + site.link(name.toString(), field.toString());
            }
        }
        return null;
    }

    public static String message(String title, Object field, String postfix) {
        if (field != null) {
            String string = field.toString();
            if (StringUtils.isNotEmpty(string)) {
                return title + ": " + field.toString() + " " + postfix;
            }
        }
        return null;
    }

    public static String messages(String title, Collection<String> fields) {
        return messages(null, title, fields);
    }

    public static String messages(String emoji, String title, Collection<String> fields) {
        if (fields.size() == 1) {
            return message(title, fields.iterator().next());
        }

        if (VKUtils.isNotEmpty(fields)) {
            List<String> page = Lists.newArrayList();
            page.add(title + ":");
            for (String field : fields) {
                page.add((emoji == null ? " - " : emoji + " ") + field);
            }
            return String.join("\n", page);
        }
        return null;
    }
}
