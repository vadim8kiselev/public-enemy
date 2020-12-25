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

    public static String dateAndTime(Long timestamp) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @SneakyThrows
    public static void randomWait() {
        long wait = 1000 + (long) (new Random().nextDouble() * (double) (1500 - 1000));
        Thread.sleep(wait);
    }
}