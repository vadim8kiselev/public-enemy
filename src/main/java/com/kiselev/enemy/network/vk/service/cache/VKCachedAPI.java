package com.kiselev.enemy.network.vk.service.cache;

import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.vk.api.internal.VKAPI;
import com.kiselev.enemy.network.vk.api.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKCachedAPI extends VKAPI {

    private final Mongo mongo;

    @Override
    public Profile profile(String profileId) {
        Profile profile = mongo.vk().readCache(profileId);
        if (profile == null) {
            profile = super.profile(profileId);
            if (profile != null) {
                mongo.vk().saveCache(profile);
            }
        }
        return profile;
    }

    @Override
    public List<Profile> profiles(List<String> profileIds) {
        return profileIds.stream()
                .map(this::profile)
                .collect(Collectors.toList());
    }
}
