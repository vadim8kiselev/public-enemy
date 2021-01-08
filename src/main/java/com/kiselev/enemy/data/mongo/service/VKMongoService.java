package com.kiselev.enemy.data.mongo.service;

import com.kiselev.enemy.data.mongo.repository.MongoVKRepository;
import com.kiselev.enemy.data.mongo.repository.cache.MongoCacheVKRepository;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VKMongoService {

    private final MongoVKRepository mongoVKRepository;

    private final MongoCacheVKRepository mongoCacheVKRepository;

    public VKProfile read(String id) {
        return mongoVKRepository.findTopByIdOrderByTimestamp(id);
    }

    public void save(VKProfile vkProfile) {
        mongoVKRepository.save(vkProfile);
    }

    public Profile readCache(String profileId) {
        return mongoCacheVKRepository.findOneByIdOrScreenName(profileId);
    }

    public void saveCache(Profile profile) {
        mongoCacheVKRepository.save(profile);
    }
}
