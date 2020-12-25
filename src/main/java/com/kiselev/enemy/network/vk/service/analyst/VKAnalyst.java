package com.kiselev.enemy.network.vk.service.analyst;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.service.VKService;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.network.vk.api.VKAPI;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.model.VKProfile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKAnalyst {

    private final VKService vk;

    @SneakyThrows
    public Analysis<VKProfile> analyze(String identifier) {
        VKProfile profile = vk.profile(identifier);

        List<Message<VKProfile>> messages = Lists.newArrayList();

        messages.add(age(profile));
        messages.add(country(profile));
        messages.add(city(profile));
        //messages.add(interests(profile));
        //messages.add(relatives(profile));
        //messages.add(lover(profile));

        return Analysis.of(
                IOUtils.toByteArray(profile.photo()),
                messages.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    public Message<VKProfile> age(VKProfile profile) {
        String age = profile.age();

        List<VKProfile> friends = profile.friends();
        Prediction<String> predictedAge = AnalyticsUtils.predict(VKProfile::age, friends);

        if (age != null && predictedAge != null) {
            if (!Objects.equals(age, predictedAge.value())
                    && predictedAge.percent() > 30) {
                return Message.of(
                        profile,
                        String.format("Has age %s but predicted one is %s \\(%s%%\\)",
                                age, predictedAge.value(), predictedAge.percent()));
            }
        }
        if (age == null && predictedAge != null) {
            return Message.of(
                    profile,
                    String.format("Has age hidden but predicted one is %s \\(%s%%\\)",
                            predictedAge.value(), predictedAge.percent()));
        }
        return null;
    }

    public Message<VKProfile> country(VKProfile profile) {
        String country = profile.country();

        List<VKProfile> friends = profile.friends();
        Prediction<String> predictedCountry = AnalyticsUtils.predict(VKProfile::country, friends);

        if (country != null && predictedCountry != null) {
            if (!Objects.equals(country, predictedCountry.value())) {
                return Message.of(
                        profile,
                        String.format("Has probably\\(%s%%\\) moved from %s to %s",
                                predictedCountry.percent(), predictedCountry.value(), country));
            }
        }
        if (country == null && predictedCountry != null) {
            return Message.of(
                    profile,
                    String.format("Has country hidden but predicted one is %s \\(%s%%\\)",
                            predictedCountry.value(), predictedCountry.percent()));
        }
        return null;
    }

    public Message<VKProfile> city(VKProfile profile) {
        String city = profile.city();

        List<VKProfile> friends = profile.friends();
        Prediction<String> predictedCity = AnalyticsUtils.predict(VKProfile::city, friends);

        if (city != null && predictedCity != null) {
            if (!Objects.equals(city, predictedCity.value())) {
                return Message.of(
                        profile,
                        String.format("Has probably\\(%s%%\\) moved from %s to %s",
                                predictedCity.percent(), predictedCity.value(), city));
            }
        }
        if (city == null && predictedCity != null) {
            return Message.of(
                    profile,
                    String.format("Has city hidden but predicted one is %s \\(%s%%\\)",
                            predictedCity.value(), predictedCity.percent()));
        }
        return null;
    }

    public Message<VKProfile> relatives(VKProfile profile) {
        List<VKProfile> relatives = profile.relatives();

        // Analyze friends

        if (CollectionUtils.isNotEmpty(relatives)) {
            StringBuilder stringBuilder = new StringBuilder("Relatives:\n");
            for (VKProfile relative : relatives) {
                stringBuilder.append(relative.name()).append("\n");
            }

            return Message.of(
                    profile,
                    stringBuilder.toString()
            );
        }
        return null;
    }

    public Message<VKProfile> lover(VKProfile profile) {
        VKProfile lover = findMostInteractivePeople(profile);

        if (lover != null) {
            return Message.of(
                    profile,
                    String.format("Lover:\n%s", lover.name())
            );
        }
        return null;
    }

    private VKProfile findMostInteractivePeople(VKProfile profile) {
        List<VKProfile> profileLikers = topLikers(profile);

        Map<VKProfile, Integer> map = profileLikers.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        liker -> index(profileLikers, liker) + index(topLikers(liker), profile)
                ));

        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Integer index(List<VKProfile> list, VKProfile profile) {
        if (list.contains(profile)) {
            return list.indexOf(profile) + 1;
        }
        return 1000;
    }

    private List<VKProfile> topLikers(VKProfile profile) {
        List<VKProfile> likers = Lists.newArrayList();

        List<Photo> photos = profile.photos();
        List<VKProfile> photosLikers = photos.stream()
                .map(Photo::likes)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        likers.addAll(photosLikers);

        List<Post> posts = profile.posts();
        List<VKProfile> postsLikers = posts.stream()
                .map(Post::likes)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        likers.addAll(postsLikers);

        Map<VKProfile, Long> likersHeatMap = likers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return likersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<VKProfile, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());
    }
}
