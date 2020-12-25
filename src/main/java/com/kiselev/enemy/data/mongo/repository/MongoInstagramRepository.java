package com.kiselev.enemy.data.mongo.repository;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoInstagramRepository extends MongoRepository<InstagramProfile, String> {

    InstagramProfile findTopByUsernameOrderByTimestamp(String username);
}