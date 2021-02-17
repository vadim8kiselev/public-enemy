package com.kiselev.enemy.network.vk.service;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.vk.api.internal.VKAPI;
import com.kiselev.enemy.network.vk.api.model.*;
import com.kiselev.enemy.network.vk.api.request.SearchRequest;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiPrivateProfileException;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.users.Relative;
import com.vk.api.sdk.objects.users.UserRelation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
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
        String age = searchAge(profile, 1, 100);

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
            LocalDateTime timestamp =
                    LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(photo.date()),
                            ZoneId.systemDefault());

            if (timestamp.isAfter(LocalDateTime.now().minusYears(1))) {
                photo.likes(
                        api.likes(id, photo.id(), Type.PHOTO).stream()
                                .map(VKInternalProfile::new)
                                .collect(Collectors.toList()));
            }
        }

        return photos;
    }

    public List<VKProfile> friends(String id) {
        List<Profile> friends = api.friends(id);

        return friends.stream()
                .map(VKInternalProfile::new)
                .collect(Collectors.toList());
    }

    public List<VKProfile> hiddenFriends(String id) {
        List<Profile> friends = api.friends(id);

        return friends.stream()
                .map(VKInternalProfile::new)
                .collect(Collectors.toList());
    }

    public List<VKProfile> followers(String id) {
        List<Profile> followers = api.followers(id);

        return followers.stream()
                .map(VKInternalProfile::new)
                .collect(Collectors.toList());
    }

    public List<VKProfile> following(String id) {
        List<Profile> following = api.following(id);

        return following.stream()
                .map(VKInternalProfile::new)
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
            LocalDateTime timestamp =
                    LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(post.date()),
                            ZoneId.systemDefault());

            if (timestamp.isAfter(LocalDateTime.now().minusYears(1))) {
                post.likes(
                        api.likes(id, post.id(), Type.POST).stream()
                                .map(VKInternalProfile::new)
                                .collect(Collectors.toList()));
            }
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

    public List<VKProfile> relatives(Profile profile) {
        List<VKProfile> relatives = Lists.newArrayList();

        List<Relative> rawRelatives = profile.relatives();

        if (rawRelatives != null) {
            List<String> relativesIds = rawRelatives.stream()
                    .map(Relative::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            List<Profile> profiles = api.profiles(relativesIds);
            relatives.addAll(profiles.stream()
                    .map(VKInternalProfile::new)
                    .collect(Collectors.toList()));
        }

        return relatives;
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
                .map(VKInternalProfile::new)
                .collect(Collectors.toList());
    }

    public String searchAge(VKProfile profile) {
        return searchAge(profile, 1, 100);
    }

    public String searchAge(VKProfile profile, int from, int to) {
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
        return String.valueOf(from);
    }

    @SneakyThrows
    private boolean isProfileFound(VKProfile profile, int a, int b) {
        SearchRequest.Query query = request -> {
            SearchRequest searchRequest = request
                    .q(profile.fullName())
                    .city(profile.cityCode())
                    .country(profile.countryCode())
                    .hometown(profile.homeTown())
                    .ageFrom(a)
                    .ageTo(b);
            if (profile.birthDay() != null) {
                searchRequest = searchRequest.birthDay(profile.birthDay());
            }
            if (profile.birthMonth() != null) {
                searchRequest = searchRequest.birthMonth(profile.birthMonth());
            }
            if (profile.birthYear() != null) {
                searchRequest = searchRequest.birthYear(profile.birthYear());
            }
            return searchRequest;
        };

        List<VKProfile> profiles = api.search(query).stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());

        return profiles.contains(profile);
    }

    public String searchBirthDate(VKProfile profile) {
        String birthday = profile.birthDate();
        if (birthday != null) {
            String[] numbers = birthday.split("\\.");
            if (numbers.length == 3) {
                return birthday;
//                int day = Integer.parseInt(numbers[0]);
//                int month = Integer.parseInt(numbers[1]);
//                int year = Integer.parseInt(numbers[2]);
//                String birthDate = searchBirthDate(profile, day, month, year);
//                return birthDate != null ? birthDate : birthday;
            } else if (numbers.length == 2) {
                int day = Integer.parseInt(numbers[0]);
                int month = Integer.parseInt(numbers[1]);
                String birthDate = searchBirthDate(profile, day, month);
                return birthDate != null ? birthDate : birthday;
            }
        }

        return searchBirthDate(profile, null, null);
    }

    private String searchBirthDate(VKProfile profile, Integer day, Integer month) {
        String age = searchAge(profile);
        if (age != null) {
            int year = LocalDate.now().minusYears(
                    Integer.parseInt(age)
            ).getYear();
            for (int predictedYear = year - 1; predictedYear <= year + 1; predictedYear++) {
                if (isProfileFound(profile, day, month, predictedYear)) {
                    String predictedBirthDate = searchBirthDate(profile, day, month, predictedYear);
                    if (predictedBirthDate != null) {
                        return predictedBirthDate;
                    }
                } else {
                    boolean debug = true;
                }
            }
        }
        return null;
    }

    public String searchBirthDate(VKProfile profile, Integer day, Integer month, Integer year) {
        if (year != null) {
            if (month != null) {
                if (day != null) {
                    if (isProfileFound(profile, day, month, year)) {
                        return String.format("%s.%s.%s", day, month, year);
                    }
                } else {
                    // Null day
                    for (int dayIndex = 1; dayIndex <= 31; dayIndex++) {
                        if (isProfileFound(profile, dayIndex, month, year)) {
                            return searchBirthDate(profile, dayIndex, month, year);
                        }
                    }
                }
            } else {
                // Null month
                for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
                    if (isProfileFound(profile, day, monthIndex, year)) {
                        return searchBirthDate(profile, day, monthIndex, year);
                    }
                }
            }
        } else {
            // Null year
            int today = LocalDate.now().getYear();
            for (int yearIndex = today; yearIndex >= today - 100; yearIndex--) {
                if (isProfileFound(profile, day, month, yearIndex)) {
                    return searchBirthDate(profile, day, month, yearIndex);
                }
            }
        }

        return null;
    }

    @SneakyThrows
    private boolean isProfileFound(VKProfile profile, Integer day, Integer month, Integer year) {
        SearchRequest.Query query = request -> {
            SearchRequest searchRequest = request
                    .q(profile.fullName())
                    .city(profile.cityCode())
                    .country(profile.countryCode())
                    .hometown(profile.homeTown());
            if (day != null) {
                searchRequest = searchRequest.birthDay(day);
            }
            if (month != null) {
                searchRequest = searchRequest.birthMonth(month);
            }
            if (year != null) {
                searchRequest = searchRequest.birthYear(year);
            }
            return searchRequest;
        };

        List<VKProfile> profiles = api.search(query).stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());

        return profiles.contains(profile);
    }

    @SneakyThrows
    public EnemyMessage<VKProfile> info(String identifier) {
        return null;
    }

    public EnemyMessage<VKProfile> version(String identifier) {
        return null;
    }

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
        public List<VKProfile> hiddenFriends() {
            if (super.hiddenFriends() == null) {
                super.hiddenFriends(VKService.this.hiddenFriends(id()));
            }
            return super.hiddenFriends();
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

        @Override
        public List<VKProfile> relatives() {
            if (super.relatives() == null) {
                super.relatives(VKService.this.relatives(super.profile()));
            }
            return super.relatives();
        }

        @Override
        public List<VKProfile> likes() {
            if (super.likes() == null) {
                super.likes(VKService.this.likes(id()));
            }
            return super.likes();
        }
    }
}
