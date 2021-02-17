package com.kiselev.enemy.network.instagram.utils;

import lombok.SneakyThrows;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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

    @SneakyThrows
    public static void timeout() {
        sleep(1000 + (long) (new Random().nextDouble() * (double) (3000 - 1000)));
    }

    @SneakyThrows
    public static void sleep(long wait) {
        Thread.sleep(wait);
    }
}
