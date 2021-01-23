package com.kiselev.enemy.data.mongo.repository;

import com.kiselev.enemy.network.vk.model.VKProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoVKRepository extends MongoRepository<VKProfile, String> {

    String FIND_BY_ID_OR_USERNAME =
            "SELECT profile "
            + "FROM Profile profile "
            + "WHERE profile.id = :identifier OR profile.username = :identifier"
            + "ORDER BY profile.timestamp DESC"
            + "LIMIT 1";

    String FIND_BY_ID_OR_USERNAME_MONGO = "{$or: [{'id': ?0}, {'username': ?0}]}";

    @Query(FIND_BY_ID_OR_USERNAME_MONGO)
    VKProfile findTopByIdOrUsernameOrderByTimestamp(String identifier);
}