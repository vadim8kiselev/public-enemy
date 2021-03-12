package com.kiselev.enemy.network.instagram.utils;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.utils.flow.model.Info;
import lombok.SneakyThrows;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;

public class InstagramUtils {

    private static final Pattern HASH_TAG = Pattern.compile("#[^\\s#]+");

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('.')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('.')
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter();

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return collection == null
                ? Stream.empty()
                : collection.stream();
    }

    public static String dateToString(LocalDateTime timestamp) {
        return timestamp.format(FORMATTER);
    }

    public static LocalDateTime timestampToDateAndTime(Long timestamp) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
//                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static List<String> hashTags(String caption) {
        List<String> hashTags = Lists.newArrayList();

        final Matcher matcher = HASH_TAG.matcher(caption);
        while (matcher.find()) {
            hashTags.add(
                    matcher.group()
            );
        }

        return hashTags;
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

    public static <Type extends Info> List<Type> friends(List<Type> followers,
                                                         List<Type> following) {
        List<Type> friends = Lists.newArrayList();

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

    public static <Type extends Info> List<Type> unfollowers(List<Type> followers,
                                                             List<Type> following) {
        List<Type> unfollowers = Lists.newArrayList();

        if (followers != null && following != null) {
            unfollowers.addAll(following);
            unfollowers.removeAll(followers);
            return unfollowers;
        }
        return null;
    }

    public static List<InstagramProfile> unfollowings(InstagramProfile profile) {
        List<InstagramProfile> unfollowings = Lists.newArrayList();

        List<InstagramProfile> followers = profile.followers();
        List<InstagramProfile> following = profile.following();

        if (followers != null && following != null) {
            unfollowings.addAll(followers);
            unfollowings.removeAll(following);
            return unfollowings;
        }
        return null;
    }

    public static <Type extends Info> List<Type> unfollowings(List<Type> followers,
                                                              List<Type> following) {
        List<Type> unfollowing = Lists.newArrayList();

        if (followers != null && following != null) {
            unfollowing.addAll(followers);
            unfollowing.removeAll(following);
            return unfollowing;
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
