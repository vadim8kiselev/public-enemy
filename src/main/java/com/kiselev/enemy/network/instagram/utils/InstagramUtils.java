package com.kiselev.enemy.network.instagram.utils;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import lombok.SneakyThrows;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class InstagramUtils {

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return collection == null
                ? Stream.empty()
                : collection.stream();
    }

    public static LocalDateTime dateAndTime(Long timestamp) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
//                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static List<InstagramProfile> friends(InstagramProfile profile) {
        List<InstagramProfile> friends = Lists.newArrayList();

        List<InstagramProfile> followers = profile.followers();
        List<InstagramProfile> following = profile.following();

        if (followers != null && following != null) {
            friends.addAll(followers);
            friends.retainAll(following);
            return friends;
        }
        return null;
    }

    public static List<InstagramProfile> unfollowers(InstagramProfile profile) {
        List<InstagramProfile> unfollowers = Lists.newArrayList();

        List<InstagramProfile> followers = profile.followers();
        List<InstagramProfile> following = profile.following();

        if (followers != null && following != null) {
            unfollowers.addAll(following);
            unfollowers.removeAll(followers);
            return unfollowers;
        }
        return null;
    }

    public static <Type> boolean areItemsUnique(List<Type> list) {
        long followingSize = list.size();

        long followingUniqueSize = list.stream()
                .distinct()
                .count();

        return followingSize == followingUniqueSize;
    }

    public static <Type> boolean areItemsNotUnique(List<Type> list) {
        long followingSize = list.size();

        long followingUniqueSize = list.stream()
                .distinct()
                .count();

        return followingSize != followingUniqueSize;
    }

    @SneakyThrows
    public static void timeout() {
        sleep(1000 + (long) (new Random().nextDouble() * (double) (3000 - 1000)));
    }

    @SneakyThrows
    public static void sleep(long wait) {
        Thread.sleep(wait);
    }
}
