package com.kiselev.enemy.service.profiler;

import com.kiselev.enemy.data.telegram.utils.TelegramUtils;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEnemyProfiler {

    private final PublicEnemyService publicEnemyService;

    public Object profile(String url) {
        String vkId = TelegramUtils.identifier(SocialNetwork.VK, url);
        if (vkId != null) {
            return vkProfile(vkId);
        }

        String igId = TelegramUtils.identifier(SocialNetwork.IG, url);
        if (igId != null) {
            return igProfile(igId);
        }

        return null;
    }

    private InstagramProfile vkProfile(String vkId) {
        Profile vkProfile = publicEnemyService.vk().api().profile(vkId);

        List<Profile> vkFriends = publicEnemyService.vk().api().friends(vkProfile.id());

        List<User> igFriends = vkFriends.stream()
                .map(Profile::instagram)
                .filter(Objects::nonNull)
                .map(publicEnemyService.ig().api()::profile)
                .collect(Collectors.toList());

        Set<com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile> predictedProfiles = igFriends.stream()
                .map(User::getPk)
                .map(publicEnemyService.ig().api().raw()::friends)
                .flatMap(List::stream)
                .filter(profile -> vkProfile.fullName().equals(profile.getName()))
                .collect(Collectors.toSet());

        return null;
    }

    private VKProfile igProfile(String igId) {
        InstagramProfile profile = publicEnemyService.ig().profile(igId);

        return null;
    }

    public InstagramProfile profile(VKProfile profile) {
        return null;
    }

    public VKProfile profile(InstagramProfile profile) {
        return null;
    }
}
