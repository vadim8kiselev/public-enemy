package com.kiselev.enemy.data.mongo.service;

import com.kiselev.enemy.data.mongo.repository.MongoInstagramRepository;
import com.kiselev.enemy.data.mongo.repository.cache.MongoCacheInstagramRepository;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstagramMongoService {

    private final MongoInstagramRepository mongoInstagramRepository;

    private final MongoCacheInstagramRepository mongoCacheInstagramRepository;

    public InstagramProfile read(String username) {
        return mongoInstagramRepository.findTopByUsernameOrderByTimestamp(username);
    }

    public void save(InstagramProfile profile) {
        mongoInstagramRepository.save(profile);
    }

    public User readCache(String username) {
        return mongoCacheInstagramRepository.findOneByUsername(username);
    }

    public void saveCache(User profile) {
        mongoCacheInstagramRepository.save(profile);
    }
}
