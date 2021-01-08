package com.kiselev.enemy.network.vk.service.analyst;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.api.request.SearchRequest;
import com.kiselev.enemy.network.vk.service.VKService;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.vk.api.sdk.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VKAnalyst {

    private final VKService vk;

    @SneakyThrows
    public Analysis<VKProfile> analyze(String identifier) {
        VKProfile profile = vk.profile(identifier);

        List<EnemyMessage<VKProfile>> messages = Lists.newArrayList();

        try {
            messages.add(age(profile));
            messages.add(country(profile));
            messages.add(city(profile));
            messages.add(likes(profile));
        } catch (Exception exception) {
            log.error("Exception while analysis of vk profile {}:\n", profile.name(), exception);
        }

//        messages.add(interests(profile));
//        messages.add(relatives(profile));
//        messages.add(lover(profile));

        return Analysis.of(
                IOUtils.toByteArray(profile.photo()),
                messages.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    public EnemyMessage<VKProfile> age(VKProfile profile) {
        Integer age = profile.age();

        List<VKProfile> friends = profile.friends();
        Prediction<Integer> predictedAge = AnalyticsUtils.predict(VKProfile::age, friends);

        if (age != null && predictedAge != null) {
            if (!Objects.equals(age, predictedAge.value()) && predictedAge.statistics() > 20) {
                return EnemyMessage.of(
                        profile,
                        String.format("Has age %s but predicted one is %s \\(%s%%\\)",
                                age, predictedAge.value(), predictedAge.statistics()));
            }
        }

        if (age == null) {
            Integer hiddenAge = searchAge(profile, 1, 100);

            if (hiddenAge != null && predictedAge == null) {
                return EnemyMessage.of(
                        profile,
                        String.format("Has hidden age %s",
                                hiddenAge));
            }
            if (hiddenAge == null && predictedAge != null) {
                return EnemyMessage.of(
                        profile,
                        String.format("Has age hidden but predicted one is %s \\(%s%%\\)",
                                predictedAge.value(), predictedAge.statistics()));
            }
            if (hiddenAge != null && predictedAge != null) {
                if (hiddenAge.equals(predictedAge.value())) {
                    return EnemyMessage.of(
                            profile,
                            String.format("Has hidden age %s which is equal to predicted one \\(%s%%\\)",
                                    hiddenAge, predictedAge.statistics()));
                } else {
                    if (predictedAge.statistics() > 20) {
                        return EnemyMessage.of(
                                profile,
                                String.format("Has hidden age %s but predicted one is %s \\(%s%%\\)",
                                        hiddenAge, predictedAge.value(), predictedAge.statistics()));
                    } else {
                        return EnemyMessage.of(
                                profile,
                                String.format("Has hidden age %s",
                                        hiddenAge));

                    }
                }
            }
        }
        return null;
    }

    public Integer searchAge(VKProfile profile, int from, int to) {
        if (from != to) {
            int middle = from + (to - from) / 2;

            boolean left = isProfileFound(profile, from, middle);
            if (left) {
                return searchAge(profile, from, middle);
            }

            boolean right = isProfileFound(profile, middle + 1, to);
            if (right) {
                return searchAge(profile, middle + 1, to);
            }

            return null;
        }
        return from;
    }

    @SneakyThrows
    private boolean isProfileFound(VKProfile profile, int a, int b) {
        List<VKProfile> profiles = vk.search(request -> request
                .q(profile.fullName())
                .city(profile.cityCode())
                .country(profile.countryCode())
                .hometown(profile.homeTown())
                .ageFrom(a)
                .ageTo(b));

        List<String> ids = profiles.stream()
                .map(VKProfile::id)
                .collect(Collectors.toList());

        return ids.contains(profile.id());
    }

    public EnemyMessage<VKProfile> country(VKProfile profile) {
        String country = profile.country();

        List<VKProfile> friends = profile.friends();
        Prediction<String> predictedCountry = AnalyticsUtils.predict(VKProfile::country, friends);

        if (country != null && predictedCountry != null) {
            if (!Objects.equals(country, predictedCountry.value())) {
                return EnemyMessage.of(
                        profile,
                        String.format("Has probably \\(%s%%\\) moved from %s to %s",
                                predictedCountry.statistics(), predictedCountry.value(), country));
            }
        }
        if (country == null && predictedCountry != null) {
            return EnemyMessage.of(
                    profile,
                    String.format("Has country hidden but predicted one is %s \\(%s%%\\)",
                            predictedCountry.value(), predictedCountry.statistics()));
        }
        return null;
    }

    public EnemyMessage<VKProfile> city(VKProfile profile) {
        String city = profile.city();

        List<VKProfile> friends = profile.friends();
        Prediction<String> predictedCity = AnalyticsUtils.predict(VKProfile::city, friends);

        if (city != null && predictedCity != null) {
            if (!Objects.equals(city, predictedCity.value())) {
                return EnemyMessage.of(
                        profile,
                        String.format("Has probably \\(%s%%\\) moved from %s to %s",
                                predictedCity.statistics(), predictedCity.value(), city));
            }
        }
        if (city == null && predictedCity != null) {
            return EnemyMessage.of(
                    profile,
                    String.format("Has city hidden but predicted one is %s \\(%s%%\\)",
                            predictedCity.value(), predictedCity.statistics()));
        }
        return null;
    }

    private EnemyMessage<VKProfile> interests(VKProfile profile) {
        List<Group> communities = profile.communities();

        List<Group> topCommunities = communities.stream()
//                .sorted(Comparator.comparing(Group::membersCount, Comparator.reverseOrder()))
                .limit(Analysis.LIMIT_LIST)
                .collect(Collectors.toList());

        String communitiesResult = topCommunities.stream()
                .map(Group::name)
                .collect(Collectors.joining("\n"));

        if (StringUtils.isNotEmpty(communitiesResult)) {
            return EnemyMessage.of(
                    profile,
                    String.format("Has following groups sorted by members:\n%s", communitiesResult)
            );
        }
        return null;
    }

    public EnemyMessage<VKProfile> likes(VKProfile profile) {
        List<VKProfile> likes = profile.likes();

        Map<VKProfile, Long> likesHeatMap = likes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String likesResult = likesHeatMap.entrySet().stream()
                .sorted(Map.Entry.<VKProfile, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey().name() + " - " + entry.getValue())
                .limit(Analysis.LIMIT_LIST)
                .collect(Collectors.joining("\n"));

        if (StringUtils.isNotEmpty(likesResult)) {
            return EnemyMessage.of(
                    profile,
                    String.format("Has following people sorted by likes on page:\n%s", likesResult)
            );
        }
        return null;
    }

//    public EnemyMessage<VKProfile> relatives(VKProfile profile) {
//        List<VKProfile> relatives = profile.relatives();
//
//        // Analyze friends
//
//        if (CollectionUtils.isNotEmpty(relatives)) {
//            StringBuilder stringBuilder = new StringBuilder("Relatives:\n");
//            for (VKProfile relative : relatives) {
//                stringBuilder.append(relative.name()).append("\n");
//            }
//
//            return EnemyMessage.of(
//                    profile,
//                    stringBuilder.toString()
//            );
//        }
//        return null;
//    }
//
//    public EnemyMessage<VKProfile> lover(VKProfile profile) {
//        VKProfile lover = findMostInteractivePeople(profile);
//
//        if (lover != null) {
//            return EnemyMessage.of(
//                    profile,
//                    String.format("Lover:\n%s", lover.name())
//            );
//        }
//        return null;
//    }
//
//    private VKProfile findMostInteractivePeople(VKProfile profile) {
//        List<VKProfile> profileLikers = AnalyticsUtils.topObjects(profile.likes(), 3);
//
//        Map<VKProfile, Integer> map = profileLikers.stream()
//                .collect(Collectors.toMap(
//                        Function.identity(),
//                        liker -> index(profileLikers, liker) + index(AnalyticsUtils.topObjects(liker.likes(), 3), profile)
//                ));
//
//        return map.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse(null);
//    }
//
//    private Integer index(List<VKProfile> list, VKProfile profile) {
//        if (list.contains(profile)) {
//            return list.indexOf(profile) + 1;
//        }
//        return 1000;
//    }
}
