package com.kiselev.enemy.network.vk;

import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.service.VKService;
import com.kiselev.enemy.network.vk.service.analyst.VKAnalyst;
import com.kiselev.enemy.network.vk.service.tracker.VKTracker;
import com.kiselev.enemy.utils.flow.SocialNetwork;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VK implements SocialNetwork<VKProfile> {

    private final VKService vk;

    private final VKTracker tracker;

    private final VKAnalyst analyst;

    public VKService service() {
        return vk;
    }

    @Override
    public VKProfile me() {
        return vk.me();
    }

    @Override
    public VKProfile profile(String identifier) {
        log.info("VK profile profiling for identifier {}", identifier);
        return vk.profile(identifier);
    }

    @Override
    public EnemyMessage<VKProfile> info(String identifier) {
        log.info("VK profile information for identifier {}", identifier);
        return vk.info(identifier);
    }

    @Override
    public EnemyMessage<VKProfile> version(String identifier) {
        log.info("VK profile version for identifier {}", identifier);
        return vk.version(identifier);
    }
}
