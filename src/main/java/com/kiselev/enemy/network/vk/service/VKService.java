package com.kiselev.enemy.network.vk.service;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.api.internal.VKInternalAPI;
import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.api.request.SearchRequest;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.vk.api.sdk.objects.likes.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKService {

    private final VKInternalAPI api;

    public VKInternalAPI api() {
        return api;
    }

    public VKProfile profile(String profileId) {
        Profile profile = api.profile(profileId);

        return new VKProfile(profile) {
            @Override
            public List<Photo> photos() {
                if (super.photos() == null) {
                    super.photos(VKService.this.photos(id()));
                }
                return super.photos();
            }

            @Override
            public List<VKProfile> friends() {
                if (super.friends() == null) {
                    super.friends(VKService.this.friends(id()));
                }
                return super.friends();
            }

            @Override
            public List<VKProfile> followers() {
                if (super.followers() == null) {
                    super.followers(VKService.this.followers(id()));
                }
                return super.followers();
            }

            @Override
            public List<VKProfile> following() {
                if (super.following() == null) {
                    super.following(VKService.this.following(id()));
                }
                return super.following();
            }

            @Override
            public List<Group> communities() {
                if (super.communities() == null) {
                    super.communities(VKService.this.communities(id()));
                }
                return super.communities();
            }

            @Override
            public List<Post> posts() {
                if (super.posts() == null) {
                    super.posts(VKService.this.posts(id()));
                }
                return super.posts();
            }

//            @Override
//            public List<VKProfile> relatives() {
//                if (relatives == null && isActive()) {
//                    List<Relative> relatives = profile.relatives();
//
//                    if (relatives != null) {
//                        List<String> relativesIds = relatives.stream()
//                                .map(Relative::getId)
//                                .map(String::valueOf)
//                                .collect(Collectors.toList());
//
//                        this.relatives = wrap(api, api.profiles(relativesIds), force);
//                    }
//                }
//                if (relatives == null) {
//                    this.relatives = Lists.newArrayList();
//                }
//                return relatives;
//            }


            @Override
            public List<VKProfile> likes() {
                if (super.likes() == null) {
                    super.likes(VKService.this.likes(id()));
                }
                return super.likes();
            }
        };
    }

    public List<Photo> photos(String id) {
        List<Photo> photos = api.photos(id);

        for (Photo photo : photos) {
            photo.likes(
                    api.likes(id, photo.id(), Type.PHOTO).stream()
                            .map(VKProfile::new)
                            .collect(Collectors.toList()));
        }

        return photos;
    }

    public List<VKProfile> friends(String id) {
        List<Profile> friends = api.friends(id);

        return friends.stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());
    }

    public List<VKProfile> followers(String id) {
        List<Profile> followers = api.followers(id);

        return followers.stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());
    }

    public List<VKProfile> following(String id) {
        List<Profile> following = api.following(id);

        return following.stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());
    }

    public List<Group> communities(String id) {
        return Lists.newArrayList(
                api.communities(id)
        );
    }

    public List<Post> posts(String id) {
        List<Post> posts = api.posts(id);

        for (Post post : posts) {
            post.likes(
                    api.likes(id, post.id(), Type.POST).stream()
                            .map(VKProfile::new)
                            .collect(Collectors.toList()));
        }

        return posts;
    }

    public List<VKProfile> likes(String id) {
        List<VKProfile> likes = Lists.newArrayList();

        List<Photo> photos = photos(id);
        List<VKProfile> photoslikes = photos.stream()
                .map(Photo::likes)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        likes.addAll(photoslikes);

        List<Post> posts = posts(id);
        List<VKProfile> postslikes = posts.stream()
                .map(Post::likes)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        likes.addAll(postslikes);

        return likes;
    }

    public List<VKProfile> search(SearchRequest.Query query) {
        return api.search(query).stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());
    }
}
