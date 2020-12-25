package com.kiselev.enemy.network.instagram.service.analyst;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.instagram.service.cache.InstagramCachedAPI;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramAnalyst {

    private final InstagramService instagram;

    @SneakyThrows
    public Analysis<InstagramProfile> analyze(String identifier) {
        InstagramProfile profile = instagram.profile(identifier);

        List<Message<InstagramProfile>> messages = Lists.newArrayList();

        messages.add(unmutuals(profile));

        return Analysis.of(
                IOUtils.toByteArray(profile.photo()),
                messages.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private Message<InstagramProfile> unmutuals(InstagramProfile profile) {
        List<InstagramProfile> followers = profile.followers();
        List<InstagramProfile> following = profile.following();

        following.removeAll(followers);

        return Message.of(
                profile,
                String.format(
                        "Has %s not following back people:\n%s",
                        following.size(),
                        following.stream()
                                .map(InstagramProfile::name)
                                .collect(Collectors.joining("\n"))));
    }
}
