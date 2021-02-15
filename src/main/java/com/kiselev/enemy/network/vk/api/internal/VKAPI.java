package com.kiselev.enemy.network.vk.api.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kiselev.enemy.network.vk.api.constants.VKConstants;
import com.kiselev.enemy.network.vk.api.model.*;
import com.kiselev.enemy.network.vk.api.request.*;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import com.vk.api.sdk.exceptions.ApiAccessAlbumException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.objects.base.UserGroupFields;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.users.Fields;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    private List<String> tokens;

    private String token() {
        return tokens.stream()
                .sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No vk tokens found!"));
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
        return new ProfileRequest(token())
                .userIds(profileIds)
                .fields(Fields.values())
                .execute();
    }

    @SneakyThrows
    public List<Photo> photos(String profileId) {
        List<Photo> photos = Lists.newArrayList();
        photos.addAll(albumPhotos(profileId, VKConstants.PROFILE_ALBUM));
        photos.addAll(albumPhotos(profileId, VKConstants.WALL_ALBUM));
//        try {
//            photos.addAll(albumPhotos(profileId, VKConstants.SAVED_ALBUM)); TODO
//        } catch (Exception exception) {
//            log.warn(exception.getMessage(), exception);
//        }

        return photos.stream()
                .sorted(Comparator.comparing(Photo::date, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Photo> albumPhotos(String profileId, String albumId) {
        try {
            int offset = 0;
            List<Photo> photos = Lists.newArrayList();

            PhotoRequest request = new PhotoRequest(token())
                    .ownerId(profileId)
                    .extended(true)
                    .albumId(albumId)
                    .count(VKConstants.PHOTOS);

            List<Photo> page = request.offset(offset).execute().getPhotos();
            photos.addAll(page);

            while (VKConstants.PHOTOS.equals(page.size())) {
                page = request.offset(offset).execute().getPhotos();
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

            FriendRequest request = new FriendRequest(token())
                    .fields(com.vk.api.sdk.objects.users.Fields.values())
                    .userId(profileId)
                    .count(VKConstants.FRIENDS);

            List<Profile> page = request.offset(offset).execute().getFriends();
            if (page != null) {
                friends.addAll(page);

                while (VKConstants.FRIENDS.equals(page.size())) {
                    page = request.offset(offset).execute().getFriends();
                    friends.addAll(page);
                    offset += VKConstants.FRIENDS;
                }
            }
            return friends;
        } catch (Exception exception) {
//            log.warn("API Exception: ", exception);
            return Collections.emptyList();
        }
    }

    @SneakyThrows
    public List<Profile> followers(String profileId) {
        int offset = 0;
        List<Profile> followers = Lists.newArrayList();

        FollowersRequest request = new FollowersRequest(token())
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .count(VKConstants.FOLLOWERS);

        List<Profile> page = request.offset(offset).execute().getFollowers();
        followers.addAll(page);

        while (VKConstants.FOLLOWERS.equals(page.size())) {
            page = request.offset(offset).execute().getFollowers();
            followers.addAll(page);
            offset += VKConstants.FOLLOWERS;
        }

        return followers;
    }

    @SneakyThrows
    public List<Profile> following(String profileId) {
        int offset = 0;
        List<Profile> following = Lists.newArrayList();

        FollowingRequest request = new FollowingRequest(token())
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.SUBSCRIPTIONS);

        List<Profile> page = request.offset(offset).execute().profiles();
        following.addAll(page);

        while (VKConstants.SUBSCRIPTIONS.equals(page.size())) {
            page = request.offset(offset).execute().profiles();
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

        FollowingRequest request = new FollowingRequest(token())
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.SUBSCRIPTIONS);

        List<Group> page = request.offset(offset).execute().subscriptions();
        subscriptions.addAll(page);

        while (VKConstants.SUBSCRIPTIONS.equals(page.size())) {
            page = request.offset(offset).execute().subscriptions();
            subscriptions.addAll(page);
            offset += VKConstants.SUBSCRIPTIONS;
        }

        return subscriptions;
    }

    @SneakyThrows
    public List<Group> groups(String profileId) {
        int offset = 0;
        List<Group> groups = Lists.newArrayList();

        GroupsRequest request = new GroupsRequest(token())
                .userId(profileId)
                .fields(com.vk.api.sdk.objects.groups.Fields.values())
                .extended(true)
                .count(VKConstants.GROUPS);

        List<Group> page = request.offset(offset).execute().getGroups();
        groups.addAll(page);

        while (VKConstants.GROUPS.equals(page.size())) {
            page = request.offset(offset).execute().getGroups();
            groups.addAll(page);
            offset += VKConstants.GROUPS;
        }

        return groups;
    }

    @SneakyThrows
    public List<Post> posts(String profileId) {
        int offset = 0;
        List<Post> posts = Lists.newArrayList();

        WallRequest request = new WallRequest(token())
                .fields(UserGroupFields.values())
                .ownerId(profileId)
                .count(VKConstants.WALL);

        List<Post> page = request.offset(offset).execute().getPosts();
        posts.addAll(page);

        while (VKConstants.WALL.equals(page.size())) {
            page = request.offset(offset).execute().getPosts();
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

        LikesRequest request = new LikesRequest(token(), type)
                .ownerId(profileId)
                .itemId(itemId)
                .extended(true)
                .count(VKConstants.LIKES);

        List<Profile> page = request.offset(offset).execute().getLikes();
        likes.addAll(page);

        while (VKConstants.LIKES.equals(page.size())) {
            page = request.offset(offset).execute().getLikes();
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

        ConversationRequest request = new ConversationRequest(token())
                .extended(true)
                .count(VKConstants.CONVERSATIONS);

        List<Profile> page = request.offset(offset).execute().getProfiles();
        profiles.addAll(page);

        while (VKConstants.CONVERSATIONS.equals(page.size())) {
            page = request.offset(offset).execute().getProfiles();
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

        MessageRequest request = new MessageRequest(token())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.MESSAGES);

        List<Message> page = request.offset(offset).execute().getMessages();
        messages.addAll(page);

        while (VKConstants.MESSAGES.equals(page.size())) {
            page = request.offset(offset).execute().getMessages();
            messages.addAll(page);
            offset += VKConstants.MESSAGES;
        }

        return messages;
    }

    @SneakyThrows
    public List<Profile> search(SearchRequest.Query query) {
        int offset = 0;
        List<Profile> profiles = Lists.newArrayList();
        SearchRequest request = new SearchRequest(token())
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .count(VKConstants.SEARCH);

        List<Profile> page = query.build(request).execute().getProfiles();
        profiles.addAll(page);

        while (VKConstants.SEARCH.equals(page.size())) {
            page = request.offset(offset).execute().getProfiles();
            profiles.addAll(page);
            offset += VKConstants.SEARCH;
        }

        return profiles;
    }
}
