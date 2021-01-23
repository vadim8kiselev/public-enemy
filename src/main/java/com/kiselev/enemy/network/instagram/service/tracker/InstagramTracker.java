package com.kiselev.enemy.network.instagram.service.tracker;

import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.instagram.service.tracker.flow.InstagramFlowProcessor;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramTracker {

    private static final String NEW_PROFILE = "New profile has been added to list";

    private static final String NO_CHANGES = "No changes since last snapshot";

    private final InstagramService ig;

    private final Mongo mongo;

    private final List<InstagramFlowProcessor> processors;

    public List<EnemyMessage<InstagramProfile>> track(String identifier) {
        InstagramProfile actualProfile = new InstagramProfile(ig.api().profile(identifier));
        InstagramProfile latestProfile = mongo.ig().read(identifier);

        List<EnemyMessage<InstagramProfile>> messages = flow(actualProfile, latestProfile);

        if (CollectionUtils.isNotEmpty(messages)) {
            mongo.ig().save(actualProfile);
        } else {
            return Collections.singletonList(
                    EnemyMessage.of(actualProfile, NO_CHANGES)
            );
        }

        return messages;
    }

    public List<EnemyMessage<InstagramProfile>> flow(InstagramProfile actual, InstagramProfile latest) {
        if (latest == null) {
            return Collections.singletonList(
                    EnemyMessage.of(actual, NEW_PROFILE)
            );
        }
        return processors.stream()
                .map(processor -> processor.process(actual, latest))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
