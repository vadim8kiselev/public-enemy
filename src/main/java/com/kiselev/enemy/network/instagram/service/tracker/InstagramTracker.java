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

    private static final String DEFAULT_MESSAGE = "New profile has been added to list";

    private final InstagramService ig;

    private final Mongo mongo;

    private final List<InstagramFlowProcessor> processors;

    public List<EnemyMessage<InstagramProfile>> track(String identifier) {
        InstagramProfile actualProfile = ig.profile(identifier);
        InstagramProfile latestProfile = mongo.ig().read(identifier);

        List<EnemyMessage<InstagramProfile>> messages = flow(actualProfile, latestProfile);
        if (CollectionUtils.isNotEmpty(messages)) {
            mongo.ig().save(actualProfile);
        }

        return messages;
    }

    public List<EnemyMessage<InstagramProfile>> flow(InstagramProfile actual, InstagramProfile latest) {
        if (latest == null) {
            return Collections.singletonList(
                    EnemyMessage.of(actual, DEFAULT_MESSAGE)
            );
        }

        return processors.stream()
                .map(processor -> processor.process(actual, latest))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
