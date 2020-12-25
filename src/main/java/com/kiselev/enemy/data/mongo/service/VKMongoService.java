package com.kiselev.enemy.data.mongo.service;

import com.kiselev.enemy.data.mongo.repository.MongoVKRepository;
import com.kiselev.enemy.network.vk.model.VKProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VKMongoService {

    private final MongoVKRepository mongoVKRepository;

    public VKProfile read(String id) {
        return mongoVKRepository.findTopByIdOrderByTimestamp(id);
    }

    public void save(VKProfile vkProfile) {
        mongoVKRepository.save(vkProfile);
    }
}
