package com.kiselev.enemy.data.mongo.repository.cache;

import com.kiselev.enemy.network.vk.api.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCacheVKRepository extends MongoRepository<Profile, String> {

    String FIND_BY_ID_OR_USERNAME =
            "SELECT profile "
                    + "FROM Profile profile "
                    + "WHERE profile.id = :identifier OR profile.username = :identifier"
                    + "ORDER BY profile.timestamp DESC"
                    + "LIMIT 1";

    String FIND_BY_ID_OR_USERNAME_MONGO = "{$or: [{'id': ?0}, {'username': ?0}]}";

    @Query(FIND_BY_ID_OR_USERNAME_MONGO)
    Profile findOneByIdOrUsername(String identifier);
}
