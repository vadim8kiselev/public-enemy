package com.kiselev.enemy.network.vk.utils;

import com.vk.api.sdk.objects.base.BaseObject;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VKUtils {

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return collection == null
                ? Stream.empty()
                : collection.stream();
    }

    public static boolean isNotEmpty(List<String> list) {
        List<String> nonNull = list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(nonNull);
    }

    @SneakyThrows
    public static void timeout() {
        Thread.sleep(300 + (long) (new Random().nextDouble() * (double) (500 - 300)));
    }

    public static String code(BaseObject baseObject) {
        if (baseObject != null) {
            return String.valueOf(
                    baseObject.getId()
            );
        }
        return null;
    }

    public static String title(BaseObject baseObject) {
        if (baseObject != null) {
            return baseObject.getTitle();
        }
        return null;
    }

    public static String age(String birthDate) {
        if (birthDate != null && birthDate.split("\\.").length == 3) {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("d.M.yyyy");
            LocalDate date = LocalDate.parse(birthDate, pattern);
            return String.valueOf(
                    Period.between(date, LocalDate.now()).getYears()
            );
        }
        return null;
    }

    public static Integer birthDay(String birthDate) {
        if (birthDate != null) {
            String[] numbers = birthDate.split("\\.");
            if (numbers.length >= 1) {
                return Integer.parseInt(numbers[0]);
            }
        }
        return null;
    }

    public static Integer birthMonth(String birthDate) {
        if (birthDate != null) {
            String[] numbers = birthDate.split("\\.");
            if (numbers.length >= 2) {
                return Integer.parseInt(numbers[1]);
            }
        }
        return null;
    }

    public static Integer birthYear(String birthDate) {
        if (birthDate != null) {
            String[] numbers = birthDate.split("\\.");
            if (numbers.length >= 3) {
                return Integer.parseInt(numbers[2]);
            }
        }
        return null;
    }
}
