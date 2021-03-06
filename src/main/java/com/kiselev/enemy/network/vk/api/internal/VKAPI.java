package com.kiselev.enemy.network.vk.api.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.kiselev.enemy.network.vk.api.constants.VKConstants;
import com.kiselev.enemy.network.vk.api.library.VKLibrary;
import com.kiselev.enemy.network.vk.api.model.*;
import com.kiselev.enemy.network.vk.api.request.SearchRequest;
import com.kiselev.enemy.network.vk.api.response.FriendResponse;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import com.vk.api.sdk.exceptions.ApiAccessAlbumException;
import com.vk.api.sdk.objects.likes.Type;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class VKAPI extends ProgressableAPI {

    @Value("${com.kiselev.enemy.vk.identifier:}")
    private String vkIdentifier;

    @Value("${com.kiselev.enemy.vk.access.tokens}")
    private List<String> raw_tokens;

//    private BlockingQueue<String> tokens;
//
//    @PostConstruct
//    public void initialize() {
//        this.tokens = Queues.newArrayBlockingQueue(raw_tokens.size());
//        this.tokens.addAll(raw_tokens);
//    }
//
//    @SneakyThrows
//    private String token() {
//        String token = tokens.take();
//        VKUtils.timeout();
//        tokens.offer(token);
//        return token;
//    }

    private Queue<String> tokens;

    @PostConstruct
    public void initialize() {
        this.tokens = Lists.newLinkedList(raw_tokens);
    }

    @SneakyThrows
    private String token() {
        String token = tokens.poll();
        tokens.add(token);
        return token;
    }

    public Profile me() {
        return profile(vkIdentifier);
    }

    @SneakyThrows
    public Profile profile(String profileId) {
        List<Profile> profiles = profiles(
                Collections.singletonList(profileId)
        );

        return profiles.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Profile not found by id " + profileId));
    }

    @SneakyThrows
    public List<Profile> profiles(List<String> profileIds) {
        return VKLibrary.profiles(token(), profileIds)
                .execute();
    }

    @SneakyThrows
    public List<Photo> photos(String profileId) {
        List<Photo> photos = Lists.newArrayList();
        photos.addAll(albumPhotos(profileId, VKConstants.PROFILE_ALBUM));
        photos.addAll(albumPhotos(profileId, VKConstants.WALL_ALBUM));
        try {
            photos.addAll(albumPhotos(profileId, VKConstants.SAVED_ALBUM));
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
        }

        return photos.stream()
                .sorted(Comparator.comparing(Photo::date, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Photo> albumPhotos(String profileId, String albumId) {
        try {
            int offset = 0;
            List<Photo> photos = Lists.newArrayList();

            List<Photo> page = VKLibrary.albumPhotos(token(), profileId, albumId)
                    .offset(offset).execute().getPhotos();
            photos.addAll(page);

            while (VKConstants.PHOTOS.equals(page.size())) {
                page = VKLibrary.albumPhotos(token(), profileId, albumId)
                        .offset(offset).execute().getPhotos();
                photos.addAll(page);
                offset += VKConstants.PHOTOS;
            }

            return photos;
        } catch (ApiAccessAlbumException exception) {
            // log exception
            return Lists.newArrayList();
        }
    }

    @SneakyThrows
    public List<Profile> friends(String profileId) {
        try {
            int offset = 0;
            List<Profile> friends = Lists.newArrayList();

            FriendResponse response = VKLibrary.friends(token(), profileId)
                    .offset(offset).execute();
            List<Profile> page = response.getFriends();
            friends.addAll(page);

            while (VKConstants.FRIENDS.equals(page.size())) {
                page = VKLibrary.friends(token(), profileId)
                        .offset(offset).execute().getFriends();
                friends.addAll(page);
                offset += VKConstants.FRIENDS;
            }

            return friends;
        } catch (Exception exception) {
            return Collections.emptyList();
        }
    }

    @SneakyThrows
    public List<Profile> followers(String profileId) {
        int offset = 0;
        List<Profile> followers = Lists.newArrayList();

        List<Profile> page = VKLibrary.followers(token(), profileId)
                .offset(offset).execute().getFollowers();
        followers.addAll(page);

        while (VKConstants.FOLLOWERS.equals(page.size())) {
            page = VKLibrary.followers(token(), profileId)
                    .offset(offset).execute().getFollowers();
            followers.addAll(page);
            offset += VKConstants.FOLLOWERS;
        }

        return followers;
    }

    @SneakyThrows
    public List<Profile> following(String profileId) {
        int offset = 0;
        List<Profile> following = Lists.newArrayList();

        List<Profile> page = VKLibrary.following(token(), profileId)
                .offset(offset).execute().profiles();
        following.addAll(page);

        while (VKConstants.SUBSCRIPTIONS.equals(page.size())) {
            page = VKLibrary.following(token(), profileId)
                    .offset(offset).execute().profiles();
            following.addAll(page);
            offset += VKConstants.SUBSCRIPTIONS;
        }

        return following;
    }

    @SneakyThrows
    public List<Group> communities(String profileId) {
        List<Group> communities = Lists.newArrayList();
        communities.addAll(subscriptions(profileId));
        communities.addAll(groups(profileId));
        return communities;
    }

    @SneakyThrows
    public List<Group> subscriptions(String profileId) {
        int offset = 0;
        List<Group> subscriptions = Lists.newArrayList();

        List<Group> page = VKLibrary.subscriptions(token(), profileId)
                .offset(offset).execute().subscriptions();
        subscriptions.addAll(page);

        while (VKConstants.SUBSCRIPTIONS.equals(page.size())) {
            page = VKLibrary.subscriptions(token(), profileId)
                    .offset(offset).execute().subscriptions();
            subscriptions.addAll(page);
            offset += VKConstants.SUBSCRIPTIONS;
        }

        return subscriptions;
    }

    @SneakyThrows
    public List<Group> groups(String profileId) {
        int offset = 0;
        List<Group> groups = Lists.newArrayList();

        List<Group> page = VKLibrary.groups(token(), profileId)
                .offset(offset).execute().getGroups();
        groups.addAll(page);

        while (VKConstants.GROUPS.equals(page.size())) {
            page = VKLibrary.groups(token(), profileId)
                    .offset(offset).execute().getGroups();
            groups.addAll(page);
            offset += VKConstants.GROUPS;
        }

        return groups;
    }

    @SneakyThrows
    public List<Post> posts(String profileId) {
        int offset = 0;
        List<Post> posts = Lists.newArrayList();

        List<Post> page = VKLibrary.posts(token(), profileId)
                .offset(offset).execute().getPosts();
        posts.addAll(page);

        while (VKConstants.WALL.equals(page.size())) {
            page = VKLibrary.posts(token(), profileId)
                    .offset(offset).execute().getPosts();
            posts.addAll(page);
            offset += VKConstants.WALL;
        }

        return posts.stream()
                .sorted(Comparator.comparing(Post::date, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Profile> likes(String profileId, String itemId, Type type) {
        int offset = 0;
        List<Profile> likes = Lists.newArrayList();

        List<Profile> page = VKLibrary.likes(token(), profileId, itemId, type)
                .offset(offset).execute().getLikes();
        likes.addAll(page);

        while (VKConstants.LIKES.equals(page.size())) {
            page = VKLibrary.likes(token(), profileId, itemId, type)
                    .offset(offset).execute().getLikes();
            likes.addAll(page);
            offset += VKConstants.LIKES;
        }

        return likes.stream()
                .filter(profile -> StringUtils.isEmpty(profile.deactivated()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Map<Profile, Set<Message>> history(List<String> profileIds) {
        List<Profile> profiles = profiles(profileIds);

        return profiles.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        profile -> messages(profile.id()),
                        (first, second) -> second
                ));
    }

    @SneakyThrows
    public Map<Profile, Set<Message>> history() {
        int offset = 0;
        List<Profile> profiles = Lists.newArrayList();

        List<Profile> page = VKLibrary.conversations(token())
                .offset(offset).execute().getProfiles();
        profiles.addAll(page);

        while (VKConstants.CONVERSATIONS.equals(page.size())) {
            page = VKLibrary.conversations(token())
                    .offset(offset).execute().getProfiles();
            profiles.addAll(page);
            offset += VKConstants.CONVERSATIONS;
        }

        return profiles.stream()
                .peek(profile -> progress.bar(SocialNetwork.VK, "History", profiles, profile))
                .collect(Collectors.toMap(
                        Function.identity(),
                        conversation -> messages(conversation.id()),
                        (first, second) -> second
                ));
    }

    @SneakyThrows
    public Set<Message> messages(String profileId) {
        int offset = 0;
        Set<Message> messages = Sets.newHashSet();

        List<Message> page = VKLibrary.messages(token(), profileId)
                .offset(offset).execute().getMessages();
        messages.addAll(page);

        while (VKConstants.MESSAGES.equals(page.size())) {
            page = VKLibrary.messages(token(), profileId)
                    .offset(offset).execute().getMessages();
            messages.addAll(page);
            offset += VKConstants.MESSAGES;
        }

        return messages;
    }

    @SneakyThrows
    public List<Profile> search(SearchRequest.Query query) {
        int offset = 0;
        List<Profile> profiles = Lists.newArrayList();

        List<Profile> page = query.build(
                VKLibrary.search(token())
        ).execute().getProfiles();

        profiles.addAll(page);

        while (VKConstants.SEARCH.equals(page.size())) {
            page = query.build(
                    VKLibrary.search(token())
            ).offset(offset).execute().getProfiles();
            profiles.addAll(page);
            offset += VKConstants.SEARCH;
        }

        return profiles;
    }
}
