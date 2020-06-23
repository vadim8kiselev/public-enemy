package com.kiselev.enemy.mongo.repository;

import com.kiselev.instagram.model.InstagramProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRepository extends org.springframework.data.mongodb.repository.MongoRepository<InstagramProfile, Long> {

    InstagramProfile findTopByPkOrderByTimestamp(Long pk);
}