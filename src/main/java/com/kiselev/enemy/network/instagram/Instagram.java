package com.kiselev.enemy.network.instagram;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.instagram.service.analyst.InstagramAnalyst;
import com.kiselev.enemy.network.instagram.service.cache.InstagramCachedAPI;
import com.kiselev.enemy.network.instagram.service.tracker.InstagramTracker;
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
public class Instagram implements SocialNetwork<InstagramProfile> {

    private final InstagramCachedAPI api;

    private final InstagramService ig;

    private final InstagramTracker tracker;

    private final InstagramAnalyst analyst;

    public InstagramCachedAPI api() {
        return api;
    }

    @Override
    public InstagramProfile profile(String identifier) {
        log.info("Instagram profile profiling for identifier {}", identifier);
        return ig.profile(identifier);
    }

    @Override
    public List<EnemyMessage<InstagramProfile>> track(String identifier) {
        log.info("Instagram profile tracking for identifier {}", identifier);
        return tracker.track(identifier);
    }

    @Override
    public Analysis<InstagramProfile> analyze(String identifier) {
        log.info("Instagram profile analysis for identifier {}", identifier);
        return analyst.analyze(identifier);
    }
}
