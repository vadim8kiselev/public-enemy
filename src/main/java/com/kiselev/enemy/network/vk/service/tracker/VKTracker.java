package com.kiselev.enemy.network.vk.service.tracker;

import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.service.VKService;
import com.kiselev.enemy.network.vk.service.tracker.flow.VKFlowProcessor;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKTracker {

    private static final String NEW_PROFILE = "New profile has been added to list";

    private static final String NO_CHANGES = "No changes since last snapshot";

    private final VKService vk;

    private final Mongo mongo;

    private final List<VKFlowProcessor> processors;

    public List<EnemyMessage<VKProfile>> track(String identifier) {
        VKProfile actualProfile = new VKProfile(vk.api().profile(identifier));
        VKProfile latestProfile = mongo.vk().read(identifier);

        List<EnemyMessage<VKProfile>> messages = flow(actualProfile, latestProfile);

        if (CollectionUtils.isNotEmpty(messages)) {
            mongo.vk().save(actualProfile);
        } else {
            return Collections.singletonList(
                    EnemyMessage.of(actualProfile, NO_CHANGES)
            );
        }

        return messages;
    }

    public List<EnemyMessage<VKProfile>> flow(VKProfile actual, VKProfile latest) {
        if (latest == null) {
            return Collections.singletonList(
                    EnemyMessage.of(actual, NEW_PROFILE)
            );
        }
        return processors.stream()
                .map(processor -> processor.process(actual, latest))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
