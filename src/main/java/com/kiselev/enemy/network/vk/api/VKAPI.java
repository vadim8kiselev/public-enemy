package com.kiselev.enemy.network.vk.api;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.api.constants.VKConstants;
import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.api.request.*;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.vk.api.sdk.exceptions.ApiAccessAlbumException;
import com.vk.api.sdk.objects.base.UserGroupFields;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.Fields;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKAPI {

    @Value("${com.kiselev.enemy.vk.access.token}")
    private String token;

    @SneakyThrows
    public Profile profile(String profileId) {
        VKUtils.timeout();

        List<Profile> profiles = profiles(
                Collections.singletonList(profileId)
        );

        return profiles.stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @SneakyThrows
    public List<Profile> profiles(List<String> profileIds) {
        VKUtils.timeout();

        return new ProfileRequest(token)
                .userIds(profileIds)
                .fields(Fields.values())
                .execute();
    }

    @SneakyThrows
    public List<Photo> photos(String profileId) {
        List<Photo> photos = Lists.newArrayList();
        photos.addAll(albumPhotos(profileId, VKConstants.PROFILE_ALBUM));
        photos.addAll(albumPhotos(profileId, VKConstants.WALL_ALBUM));
        //photos.addAll(albumPhotos(profileId, VKConstants.SAVED_ALBUM)); // TODO

        return photos.stream()
                .sorted(Comparator.comparing(Photo::date, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private List<Photo> albumPhotos(String profileId, String albumId) {
        try {
            VKUtils.timeout();

            int offset = 0;
            List<Photo> photos = Lists.newArrayList();

            PhotoRequest request = new PhotoRequest(token)
                    .ownerId(profileId)
                    .extended(true)
                    .albumId(albumId)
                    .count(VKConstants.PHOTOS);

            List<Photo> page = request.offset(offset).execute().getPhotos();
            photos.addAll(page);
            VKUtils.timeout();

            while (VKConstants.PHOTOS.equals(page.size())) {
                page = request.offset(offset).execute().getPhotos();
                VKUtils.timeout();
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
        VKUtils.timeout();

        int offset = 0;
        List<Profile> friends = Lists.newArrayList();

        FriendRequest request = new FriendRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .count(VKConstants.FRIENDS);

        List<Profile> page = request.offset(offset).execute().getFriends();
        friends.addAll(page);
        VKUtils.timeout();

        while (VKConstants.FRIENDS.equals(page.size())) {
            page = request.offset(offset).execute().getFriends();
            VKUtils.timeout();
            friends.addAll(page);
            offset += VKConstants.FRIENDS;
        }

        return friends;
    }

    @SneakyThrows
    public List<Profile> followers(String profileId) {
        VKUtils.timeout();

        int offset = 0;
        List<Profile> followers = Lists.newArrayList();

        FollowersRequest request = new FollowersRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .count(VKConstants.FOLLOWERS);

        List<Profile> page = request.offset(offset).execute().getFollowers();
        followers.addAll(page);
        VKUtils.timeout();

        while (VKConstants.FOLLOWERS.equals(page.size())) {
            page = request.offset(offset).execute().getFollowers();
            VKUtils.timeout();
            followers.addAll(page);
            offset += VKConstants.FOLLOWERS;
        }

        return followers;
    }

    @SneakyThrows
    public List<Profile> following(String profileId) {
        VKUtils.timeout();

        int offset = 0;
        List<Profile> following = Lists.newArrayList();

        FollowingRequest request = new FollowingRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.SUBSCRIPTIONS);

        List<Profile> page = request.offset(offset).execute().profiles();
        following.addAll(page);
        VKUtils.timeout();

        while (VKConstants.SUBSCRIPTIONS.equals(page.size())) {
            page = request.offset(offset).execute().profiles();
            VKUtils.timeout();
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
    private List<Group> subscriptions(String profileId) {
        VKUtils.timeout();

        int offset = 0;
        List<Group> subscriptions = Lists.newArrayList();

        FollowingRequest request = new FollowingRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.SUBSCRIPTIONS);

        List<Group> page = request.offset(offset).execute().subscriptions();
        subscriptions.addAll(page);
        VKUtils.timeout();

        while (VKConstants.SUBSCRIPTIONS.equals(page.size())) {
            page = request.offset(offset).execute().subscriptions();
            VKUtils.timeout();
            subscriptions.addAll(page);
            offset += VKConstants.SUBSCRIPTIONS;
        }

        return subscriptions;
    }

    @SneakyThrows
    private List<Group> groups(String profileId) {
        VKUtils.timeout();

        int offset = 0;
        List<Group> groups = Lists.newArrayList();

        GroupsRequest request = new GroupsRequest(token)
                .userId(profileId)
                .fields(com.vk.api.sdk.objects.groups.Fields.values())
                .extended(true)
                .count(VKConstants.GROUPS);

        List<Group> page = request.offset(offset).execute().getGroups();
        groups.addAll(page);
        VKUtils.timeout();

        while (VKConstants.GROUPS.equals(page.size())) {
            page = request.offset(offset).execute().getGroups();
            VKUtils.timeout();
            groups.addAll(page);
            offset += VKConstants.GROUPS;
        }

        return groups;
    }

    @SneakyThrows
    public List<Post> posts(String profileId) {
        VKUtils.timeout();

        int offset = 0;
        List<Post> posts = Lists.newArrayList();

        WallRequest request = new WallRequest(token)
                .fields(UserGroupFields.values())
                .ownerId(profileId)
                .count(VKConstants.WALL);

        List<Post> page = request.offset(offset).execute().getPosts();
        posts.addAll(page);
        VKUtils.timeout();

        while (VKConstants.WALL.equals(page.size())) {
            page = request.offset(offset).execute().getPosts();
            VKUtils.timeout();
            posts.addAll(page);
            offset += VKConstants.WALL;
        }

        return posts.stream()
                .sorted(Comparator.comparing(Post::date, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Profile> likes(String profileId, String itemId, Type type) {
        VKUtils.timeout();

        int offset = 0;
        List<Profile> likes = Lists.newArrayList();

        LikesRequest request = new LikesRequest(token, type)
                .ownerId(profileId)
                .itemId(itemId)
                .extended(true)
                .count(VKConstants.LIKES);

        List<Profile> page = request.offset(offset).execute().getLikes();
        likes.addAll(page);
        VKUtils.timeout();

        while (VKConstants.LIKES.equals(page.size())) {
            page = request.offset(offset).execute().getLikes();
            VKUtils.timeout();
            likes.addAll(page);
            offset += VKConstants.LIKES;
        }

        return likes.stream()
                .filter(profile -> StringUtils.isEmpty(profile.deactivated()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Map<Profile, List<Message>> history() {
        VKUtils.timeout();

        int offset = 0;
        List<Profile> conversations = Lists.newArrayList();

        ConversationRequest request = new ConversationRequest(token)
                .extended(true)
                .count(VKConstants.CONVERSATIONS);

        List<Profile> page = request.offset(offset).execute().getProfiles();
        conversations.addAll(page);
        VKUtils.timeout();

        while (VKConstants.CONVERSATIONS.equals(page.size())) {
            page = request.offset(offset).execute().getProfiles();
            VKUtils.timeout();
            conversations.addAll(page);
            offset += VKConstants.CONVERSATIONS;
        }

        return conversations.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        conversation -> messages(conversation.id())
                ));
    }

    @SneakyThrows
    public List<Message> messages(String profileId) {
        VKUtils.timeout();

        int offset = 0;
        List<Message> messages = Lists.newArrayList();

        MessageRequest request = new MessageRequest(token)
                .userId(profileId)
                .extended(true)
                .count(VKConstants.MESSAGES);

        List<Message> page = request.offset(offset).execute().getMessages();
        messages.addAll(page);
        VKUtils.timeout();

        while (VKConstants.MESSAGES.equals(page.size())) {
            page = request.offset(offset).execute().getMessages();
            VKUtils.timeout();
            messages.addAll(page);
            offset += VKConstants.MESSAGES;
        }

        return messages.stream()
                .sorted(Comparator.comparing(Message::getDate).reversed())
                .collect(Collectors.toList());
    }
}
