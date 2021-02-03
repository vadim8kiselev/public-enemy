package com.kiselev.enemy.network.instagram.service.cache;

import com.google.common.collect.Lists;
import com.kiselev.enemy.data.mongo.Mongo;
import com.kiselev.enemy.network.instagram.api.InstagramAPI;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramCachedAPI extends ProgressableAPI {

    private final InstagramAPI api;

    private final Mongo mongo;

    @Value("${com.kiselev.enemy.instagram.identifier:}")
    private String igIdentifier;

    public InstagramAPI raw() {
        return api;
    }

    public User me() {
        return profile(igIdentifier);
    }

    public User profile(String username) {
        User profile = mongo.ig().readCache(username);
        if (profile == null) {
            profile = api.profile(username);
            if (profile != null && !profile.isDeleted()) {
                mongo.ig().saveCache(profile);
            }
        }
        return profile;
    }

    public List<User> friends(String profilePk) {
        List<Profile> friends = api.friends(profilePk);

        return friends.stream()
                .peek(friend -> progress.bar(SocialNetwork.IG, "Friends", friends, friend))
                .map(Profile::username)
                .map(this::profile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> followers(String profilePk) {
        List<Profile> followers = api.followers(profilePk);

        return followers.stream()
                .peek(follower -> progress.bar(SocialNetwork.IG, "Followers", followers, follower))
                .map(Profile::username)
                .map(this::profile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> following(String profilePk) {
        List<Profile> followings = api.following(profilePk);

        return followings.stream()
                .peek(following -> progress.bar(SocialNetwork.IG, "Following", followings, following))
                .map(Profile::username)
                .map(this::profile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<TimelineMedia> posts(String profilePk) {
        return Lists.newArrayList(
                api.posts(profilePk)
        );
    }

    public List<User> likes(String mediaId) {
        List<Profile> likes = api.likes(mediaId);

        return likes.stream()
                .peek(like -> progress.bar(SocialNetwork.IG, "Likes", likes, like))
                .map(Profile::username)
                .map(this::profile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Comment> commentaries(String mediaId) {
        return api.commentaries(mediaId);
    }

    public List<ReelMedia> stories(Long profilePk) {
        return api.stories(profilePk);
    }

    public List<User> viewers(String storyId) {
        List<Profile> viewers = api.viewers(storyId);

        return viewers.stream()
                .peek(like -> progress.bar(SocialNetwork.IG, "Viewers", viewers, like))
                .map(Profile::username)
                .map(this::profile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Map<Profile, Set<ThreadItem>> history() {
        return api.history();
    }

    public Set<ThreadItem> messages(String threadId) {
        return api.messages(threadId);
    }
}
