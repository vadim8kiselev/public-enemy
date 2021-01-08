package com.kiselev.enemy.network.vk.api;

import com.kiselev.enemy.network.vk.api.model.*;
import com.kiselev.enemy.network.vk.api.request.SearchRequest;
import com.vk.api.sdk.objects.likes.Type;

import java.util.List;
import java.util.Map;

public interface VKAPI {

    Profile profile(String profileId);

    List<Profile> profiles(List<String> profileIds);

    List<Photo> photos(String profileId);

    List<Photo> albumPhotos(String profileId, String albumId);

    List<Profile> friends(String profileId);

    List<Profile> followers(String profileId);

    List<Profile> following(String profileId);

    List<Group> communities(String profileId);

    List<Group> subscriptions(String profileId);

    List<Group> groups(String profileId);

    List<Post> posts(String profileId);

    List<Profile> likes(String profileId, String itemId, Type type);

    Map<Profile, List<Message>> history(List<String> profileIds);

    Map<Profile, List<Message>> history();

    List<Message> messages(String profileId);

    List<Profile> search(SearchRequest.Query query);
}
