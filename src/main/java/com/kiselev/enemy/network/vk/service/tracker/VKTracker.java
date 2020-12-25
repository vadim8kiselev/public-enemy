package com.kiselev.enemy.network.vk.service.tracker;

import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.service.VKService;
import com.kiselev.enemy.network.vk.service.tracker.flow.VKFlowProcessor;
import com.kiselev.enemy.utils.flow.message.Message;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKTracker {

    private static final String DEFAULT_MESSAGE = "New profile has been added to list";

    private final VKService vk;

    private final Mongo mongo;

    private final List<VKFlowProcessor> processors;

    public List<Message<VKProfile>> track(String identifier) {
        VKProfile actualProfile = vk.profile(identifier);
        VKProfile latestProfile = mongo.vk().read(identifier);

        List<Message<VKProfile>> messages = flow(actualProfile, latestProfile);
        if (CollectionUtils.isNotEmpty(messages)) {
            mongo.vk().save(actualProfile);
        }

        return messages;
    }

    public List<Message<VKProfile>> flow(VKProfile actual, VKProfile latest) {
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
