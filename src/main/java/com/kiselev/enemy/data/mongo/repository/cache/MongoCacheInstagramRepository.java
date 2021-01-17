package com.kiselev.enemy.data.mongo.repository.cache;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoCacheInstagramRepository extends MongoRepository<User, String> {

    List<User> findByUsername(String username);

    User findOneByUsername(String username);
}
