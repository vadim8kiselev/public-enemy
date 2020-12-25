package com.kiselev.enemy.network.vk;

import com.kiselev.enemy.network.vk.service.tracker.VKTracker;
import com.kiselev.enemy.utils.flow.SocialNetwork;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.network.vk.service.analyst.VKAnalyst;
import com.kiselev.enemy.network.vk.service.tracker.flow.VKFlowProcessor;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.service.VKService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VK implements SocialNetwork<VKProfile> {

    private final VKService vk;

    private final VKTracker tracker;

    private final VKAnalyst analytics;

    @Override
    public VKProfile profile(String identifier) {
        return vk.profile(identifier);
    }

    @Override
    public List<Message<VKProfile>> track(String identifier) {
        return tracker.track(identifier);
    }

    @Override
    public Analysis<VKProfile> analyze(String identifier) {
        return analytics.analyze(identifier);
    }
}
