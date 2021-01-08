package com.kiselev.enemy.data.mongo.repository.cache;

import com.kiselev.enemy.network.vk.api.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCacheVKRepository extends MongoRepository<Profile, String> {

    Profile findOneByIdOrScreenName(String id);
}
