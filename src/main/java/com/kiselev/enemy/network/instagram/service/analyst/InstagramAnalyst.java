package com.kiselev.enemy.network.instagram.service.analyst;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramAnalyst {

    private final InstagramService instagram;

    @SneakyThrows
    public Analysis<InstagramProfile> analyze(String identifier) {
        InstagramProfile profile = instagram.profile(identifier);

        List<EnemyMessage<InstagramProfile>> messages = Lists.newArrayList();

        messages.add(unmutuals(profile));
//        messages.add(likes(profile));

        return Analysis.of(
                IOUtils.toByteArray(profile.photo()),
                messages.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private EnemyMessage<InstagramProfile> unmutuals(InstagramProfile profile) {
        List<InstagramProfile> unmutuals = Lists.newArrayList();

        List<InstagramProfile> followers = profile.followers();
        List<InstagramProfile> following = profile.following();

        unmutuals.addAll(following);
        unmutuals.removeAll(followers);

        if (CollectionUtils.isNotEmpty(unmutuals)) {
            return EnemyMessage.of(
                    profile,
                    String.format(
                            "Has %s not following back people:\n%s",
                            unmutuals.size(),
                            unmutuals.stream()
                                    .map(InstagramProfile::name)
                                    .collect(Collectors.joining("\n"))));
        }
        return null;
    }

    private EnemyMessage<InstagramProfile> likes(InstagramProfile profile) {
        List<InstagramProfile> likes = profile.likes();
        List<Prediction<InstagramProfile>> top = AnalyticsUtils.top(likes, Analysis.LIMIT_LIST);

        if (CollectionUtils.isNotEmpty(top)) {
            return EnemyMessage.of(
                    profile,
                    String.format(
                            "Has following people sorted by likes on page:\n%s",
                            top.stream()
                                    .map(prediction -> prediction.value().name() + " - " + prediction.statistics())
                                    .collect(Collectors.joining("\n")))
            );
        }
        return null;
    }
}
