package com.kiselev.enemy.network.instagram;

import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.instagram.service.analyst.InstagramAnalyst;
import com.kiselev.enemy.network.instagram.service.cache.InstagramCachedAPI;
import com.kiselev.enemy.network.instagram.service.tracker.InstagramTracker;
import com.kiselev.enemy.utils.flow.SocialNetwork;
import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.network.instagram.model.*;
//import com.kiselev.enemy.network.instagram.model.mapper.InstagramMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Instagram implements SocialNetwork<InstagramProfile> {

    private final InstagramService ig;

    private final InstagramTracker tracker;

    private final InstagramAnalyst analyst;

    @Override
    public InstagramProfile profile(String identifier) {
        return ig.profile(identifier);
    }

    @Override
    public List<Message<InstagramProfile>> track(String identifier) {
        return tracker.track(identifier);
    }

    @Override
    public Analysis<InstagramProfile> analyze(String identifier) {
        return analyst.analyze(identifier);
    }
}
