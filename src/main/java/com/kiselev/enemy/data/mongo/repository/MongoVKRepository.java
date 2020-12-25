package com.kiselev.enemy.data.mongo.repository;

import com.kiselev.enemy.network.vk.model.VKProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoVKRepository extends MongoRepository<VKProfile, String> {

    VKProfile findTopByIdOrderByTimestamp(String id);
}