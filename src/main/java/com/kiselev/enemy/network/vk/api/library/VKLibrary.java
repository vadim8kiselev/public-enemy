package com.kiselev.enemy.network.vk.api.library;

import com.kiselev.enemy.network.vk.api.constants.VKConstants;
import com.kiselev.enemy.network.vk.api.request.*;
import com.vk.api.sdk.objects.base.UserGroupFields;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.users.Fields;

import java.util.List;

public class VKLibrary {

    public static ProfileRequest profiles(String token, List<String> profileIds) {
        return new ProfileRequest(token)
                .userIds(profileIds)
                .fields(Fields.values());
    }

    public static PhotoRequest albumPhotos(String token, String profileId, String albumId) {
        return new PhotoRequest(token)
                    .ownerId(profileId)
                    .extended(true)
                    .albumId(albumId)
                    .count(VKConstants.PHOTOS);
    }

    public static FriendRequest friends(String token, String profileId) {
        return new FriendRequest(token)
                    .fields(com.vk.api.sdk.objects.users.Fields.values())
                    .userId(profileId)
                    .count(VKConstants.FRIENDS);
    }

    public static FollowersRequest followers(String token, String profileId) {
        return new FollowersRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .count(VKConstants.FOLLOWERS);
    }

    public static FollowingRequest following(String token, String profileId) {
        return new FollowingRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.SUBSCRIPTIONS);
    }

    public static FollowingRequest subscriptions(String token, String profileId) {
        return  new FollowingRequest(token)
                .fields(com.vk.api.sdk.objects.users.Fields.values())
                .userId(profileId)
                .extended(true)
                .count(VKConstants.SUBSCRIPTIONS);
    }

    public static GroupsRequest groups(String token, String profileId) {
        return new GroupsRequest(token)
                .userId(profileId)
                .fields(com.vk.api.sdk.objects.groups.Fields.values())
                .extended(true)
                .count(VKConstants.GROUPS);
    }

    public static WallRequest posts(String token, String profileId) {
        return new WallRequest(token)
                .fields(UserGroupFields.values())
                .ownerId(profileId)
                .count(VKConstants.WALL);
    }

    public static LikesRequest likes(String token, String profileId, String itemId, Type type) {
        return new LikesRequest(token, type)
                .ownerId(profileId)
                .itemId(itemId)
                .extended(true)
                .count(VKConstants.LIKES);
    }

    public static ConversationRequest conversations(String token) {
        return new ConversationRequest(token)
                .extended(true)
                .count(VKConstants.CONVERSATIONS);
    }

    public static MessageRequest messages(String token, String profileId) {
        return new MessageRequest(token)
                .userId(profileId)
                .extended(true)
                .count(VKConstants.MESSAGES);
    }

    public static SearchRequest search(String token) {
        return new SearchRequest(token)
                .fields(Fields.values())
                .count(VKConstants.SEARCH);
    }
}
