package com.kiselev.enemy.data.mongo.repository.cache;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCacheInstagramRepository extends MongoRepository<User, String> {

    User findOneByUsername(String username);
}
