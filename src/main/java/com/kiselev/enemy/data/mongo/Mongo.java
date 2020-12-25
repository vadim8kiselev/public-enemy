package com.kiselev.enemy.data.mongo;

import com.kiselev.enemy.data.mongo.service.InstagramMongoService;
import com.kiselev.enemy.data.mongo.service.VKMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("mongoService")
@RequiredArgsConstructor
public class Mongo {

    private final InstagramMongoService instagramMongoService;

    private final VKMongoService vkMongoService;

    public InstagramMongoService ig() {
        return instagramMongoService;
    }

    public VKMongoService vk() {
        return vkMongoService;
    }
}
