package com.kiselev.enemy.network.instagram.service.cache;

import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.instagram.api.InstagramAPI;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramFeedItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramCachedAPI {

    private final InstagramAPI api;

    private final Mongo mongo;

    public User profile(String username) {
        User profile = mongo.ig().readCache(username);
        if (profile == null) {
            profile = api.profile(username);
            if (profile != null) {
                mongo.ig().saveCache(profile);
            } else {
                throw new RuntimeException("Profile is null");
            }
        }
        return profile;
    }

    public List<User> followers(Long profilePk) {
        List<Profile> followers = api.followers(profilePk);

        return followers.stream()
                .map(Profile::getUsername)
                .map(this::profile)
                .collect(Collectors.toList());
    }

    public List<User> following(Long profilePk) {
        List<Profile> following = api.following(profilePk);

        return following.stream()
                .map(Profile::getUsername)
                .map(this::profile)
                .collect(Collectors.toList());
    }

    public List<TimelineMedia> posts(Long profilePk) {
        return api.posts(profilePk);
    }

    public List<User> likes(String mediaId) {
        List<Profile> likes = api.likes(mediaId);

        return likes.stream()
                .map(Profile::getUsername)
                .map(this::profile)
                .collect(Collectors.toList());
    }

    public List<Comment> commentaries(String mediaId) {
        return api.commentaries(mediaId);
    }
}
