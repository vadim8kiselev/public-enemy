package com.kiselev.enemy.network.instagram.service.tracker;

import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.instagram.service.tracker.flow.InstagramFlowProcessor;
import com.kiselev.enemy.utils.flow.message.Message;
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

    public List<Message<InstagramProfile>> track(String identifier) {
        InstagramProfile actualProfile = ig.profile(identifier);
        InstagramProfile latestProfile = mongo.ig().read(identifier);

        List<Message<InstagramProfile>> messages = flow(actualProfile, latestProfile);
        if (CollectionUtils.isNotEmpty(messages)) {
            mongo.ig().save(actualProfile);
        }

        return messages;
    }

    public List<Message<InstagramProfile>> flow(InstagramProfile actual, InstagramProfile latest) {
        if (latest == null) {
            return Collections.singletonList(
                    Message.of(actual, DEFAULT_MESSAGE)
            );
        }

        return processors.stream()
                .map(processor -> processor.process(actual, latest))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
