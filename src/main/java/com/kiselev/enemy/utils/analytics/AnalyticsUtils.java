package com.kiselev.enemy.utils.analytics;

import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.model.Info;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnalyticsUtils {

    public static <Profile extends Info, Type> Prediction<Type> predict(Function<Profile, Type> function,
                                                                        List<Profile> predictionGroup) {
        List<Type> predictionCandidates = predictionGroup.stream()
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<Type, Long> predictionCandidatesHeatMap = predictionCandidates.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Type, Long> sortedPredictionCandidatesHeatMap = predictionCandidatesHeatMap.entrySet().stream()
                .sorted(Map.Entry.<Type, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        Type prediction = sortedPredictionCandidatesHeatMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (prediction != null) {
            int predictionGroupSize = predictionCandidates.size();
            long predictionOccurrencesNumber = predictionCandidates.stream()
                    .filter(prediction::equals)
                    .count();

            return Prediction.of(
                    prediction,
                    (predictionOccurrencesNumber * 100) / predictionGroupSize);
        }

        return null;
    }

    public static <Profile extends Info> List<Prediction<Profile>> top(List<Profile> group, Integer limit) {
        Map<Profile, Long> likersHeatMap = group.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return likersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<Profile, Long>comparingByValue().reversed())
                .map(entry -> Prediction.of(entry.getKey(), entry.getValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static <Profile extends Info> List<Profile> topObjects(List<Profile> group, Integer limit) {
        Map<Profile, Long> likersHeatMap = group.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return likersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<Profile, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
