package com.kiselev.enemy.mongo;

import com.kiselev.enemy.mongo.repository.MongoRepository;
import com.kiselev.instagram.model.InstagramProfile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MongoService {

    private final MongoRepository mongoRepository;

    public InstagramProfile read(Long pk) {
        return mongoRepository.findTopByPkOrderByTimestamp(pk);
    }

    public void save(InstagramProfile instagramProfile) {
        mongoRepository.save(instagramProfile);
    }
}
