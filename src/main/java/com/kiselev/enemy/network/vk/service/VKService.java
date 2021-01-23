package com.kiselev.enemy.network.vk.service;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.api.internal.VKAPI;
import com.kiselev.enemy.network.vk.api.model.*;
import com.kiselev.enemy.network.vk.api.request.SearchRequest;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.vk.api.sdk.objects.likes.Type;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VKService {

    private final VKAPI api;

    public VKAPI api() {
        return api;
    }

    public VKProfile me() {
        Profile me = api.me();

        return new VKInternalProfile(me);
    }

    public VKProfile profile(String profileId) {
        if (profileId == null) {
            return null;
        }

        Profile profile = api.profile(profileId);

        return new VKInternalProfile(profile);
    }

    public String age(VKProfile profile) {
        Integer intAge = searchAge(profile, 1, 100);
        String age = intAge != null ? intAge.toString() : null;

        if (age == null) {
            List<VKProfile> friends = profile.friends();
            Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::age, friends);
            if (prediction != null) {
                age = prediction.value();
            }
        }

        return age;
    }

    public String country(VKProfile profile) {
        List<VKProfile> friends = profile.friends();
        Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::country, friends);
        if (prediction != null) {
            return prediction.value();
        }
        return null;
    }

    public String city(VKProfile profile) {
        List<VKProfile> friends = profile.friends();
        Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::city, friends);
        if (prediction != null) {
            return prediction.value();
        }
        return null;
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

    public Map<VKProfile, Set<Message>> history() {
        Map<Profile, Set<Message>> history = api.history();
        return history.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new VKInternalProfile(entry.getKey()),
                        Map.Entry::getValue,
                        (first, second) -> second
                ));
    }

    public Set<Message> messages(String profileId) {
        return api.messages(profileId);
    }

    public List<VKProfile> search(SearchRequest.Query query) {
        return api.search(query).stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());
    }

    public Integer searchAge(VKProfile profile, int from, int to) {
        if (from != to) {
            int middle = from + (to - from) / 2;

            boolean left = isProfileFound(profile, from, middle);
            if (left) {
                return searchAge(profile, from, middle);
            }

            boolean right = isProfileFound(profile, middle + 1, to);
            if (right) {
                return searchAge(profile, middle + 1, to);
            }

            return null;
        }
        return from;
    }

    @SneakyThrows
    private boolean isProfileFound(VKProfile profile, int a, int b) {
        List<VKProfile> profiles = search(request -> request
                .q(profile.fullName())
                .city(profile.cityCode())
                .country(profile.countryCode())
                .hometown(profile.homeTown())
                .ageFrom(a)
                .ageTo(b));

        List<String> ids = profiles.stream()
                .map(VKProfile::id)
                .collect(Collectors.toList());

        return ids.contains(profile.id());
    }

//    @SneakyThrows
//    public EnemyMessage<Profile> info(String identifier) {
//        List<String> messages = Lists.newArrayList();
//
//        Profile profile = api.profile(identifier);
//        messages.add(message("Username", profile.username()));
//        messages.add(message("Full name", profile.fullName()));
//        messages.add(message("Status", profile.status()));
//        messages.add(message("Sex", profile.sex().name()));
//
//        Integer age = VKUtils.age(profile.birthday());
//        if (age != null) {
//            messages.add(message("Age", age));
//        } else {
//            messages.add(message("Age", "Hidden"));
//            messages.add(message(">>>", "Hidden"));
//        }
//
//        messages.add(message("Birthday", profile.birthday()));
//        messages.add(message("Country", profile.country()));
//        messages.add(message("City", profile.city()));
//        messages.add(message("Phone", profile.phone()));
//
//        String message = messages.stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.joining("\n"));
//        if (StringUtils.isNotEmpty(message)) {
//            return EnemyMessage.of(profile, message);
//        } else {
//            return null;
//        }
//    }

//    private String message(String title, Object field) {
//        if (field != null) {
//            String string = field.toString();
//            if (StringUtils.isNotEmpty(string)) {
//                return title + ": " + field.toString();
//            }
//        }
//        return null;
//    }

    private class VKInternalProfile extends VKProfile {

        public VKInternalProfile(Profile profile) {
            super(profile);
        }

        @Override
        public String age() {
            if (super.age() == null) {
                super.age(VKService.this.age(this));
            }
            return super.age();
        }

        @Override
        public String country() {
            if (super.country() == null) {
                super.country(VKService.this.country(this));
            }
            return super.country();
        }

        @Override
        public String city() {
            if (super.city() == null) {
                super.city(VKService.this.city(this));
            }
            return super.city();
        }

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
    }
}
