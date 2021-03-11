package com.kiselev.enemy.network.instagram;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.utils.flow.SocialNetwork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Instagram implements SocialNetwork<InstagramProfile> {

    private final InstagramService ig;

//    private final InstagramTracker tracker;
//
//    private final InstagramAnalyst analyst;

    public InstagramService service() {
        return ig;
    }

    @Override
    public InstagramProfile me() {
        return ig.me();
    }

    @Override
    public InstagramProfile profile(String identifier) {
        log.info("Instagram profile profiling for identifier {}", identifier);
        return ig.profile(identifier);
    }

//    @Override
//    public EnemyMessage<InstagramProfile> info(String identifier) {
//        log.info("Instagram profile information for identifier {}", identifier);
//        return null;
//    }
//
//    @Override
//    public EnemyMessage<InstagramProfile> version(String identifier) {
//        log.info("Instagram profile version for identifier {}", identifier);
//        return null;
//    }
}
