package com.kiselev.enemy.network.vk;

import com.kiselev.enemy.network.vk.api.internal.VKInternalAPI;
import com.kiselev.enemy.network.vk.service.tracker.VKTracker;
import com.kiselev.enemy.utils.flow.SocialNetwork;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.network.vk.service.analyst.VKAnalyst;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.service.VKService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VK implements SocialNetwork<VKProfile> {

    private final VKInternalAPI api;

    private final VKService vk;

    private final VKTracker tracker;

    private final VKAnalyst analytics;

    public VKInternalAPI api() {
        return api;
    }

    @Override
    public VKProfile profile(String identifier) {
        log.info("VK profile profiling for identifier {}", identifier);
        return vk.profile(identifier);
    }

    @Override
    public List<EnemyMessage<VKProfile>> track(String identifier) {
        log.info("VK profile tracking for identifier {}", identifier);
        return tracker.track(identifier);
    }

    @Override
    public Analysis<VKProfile> analyze(String identifier) {
        log.info("VK profile analysis for identifier {}", identifier);
        return analytics.analyze(identifier);
    }
}
