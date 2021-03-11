package com.kiselev.enemy.network.vk.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kiselev.enemy.network.vk.model.Zodiac;
import com.vk.api.sdk.objects.base.BaseObject;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VKUtils {

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return collection == null
                ? Stream.empty()
                : collection.stream();
    }

    public static boolean isNotEmpty(Collection<String> list) {
        List<String> nonNull = list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(nonNull);
    }

    public static Set<String> set(String... strings) {
        return Arrays.stream(strings)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public static void timeout() {
        Thread.sleep(250 + (long) (new Random().nextDouble() * (double) (600 - 250)));
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

    public static boolean areFamilyMembers(String a_lastName, String b_lastName) {
        return Math.abs(a_lastName.length() - b_lastName.length()) <= 3
                && (a_lastName.contains(b_lastName) || b_lastName.contains(a_lastName));
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

    public static List<Zodiac> zodiacs(String birthDate) {
        return Lists.newArrayList(
                Sets.newHashSet(
                        zodiac_eng(birthDate),
                        zodiac_rus(birthDate)
                )
        );
    }

    public static Zodiac zodiac_eng(String birthDate) {
        LocalDate date = LocalDate.of(
                LocalDate.now().getYear(),
                birthMonth(birthDate),
                birthDay(birthDate));

        if (between(date, date(21, 3), date(19, 4))) {
            return Zodiac.ARIES;
        }
        if (between(date, date(20, 4), date(20, 5))) {
            return Zodiac.TAURUS;
        }
        if (between(date, date(21, 5), date(20, 6))) {
            return Zodiac.GEMINI;
        }
        if (between(date, date(21, 6), date(22, 7))) {
            return Zodiac.CANCER;
        }
        if (between(date, date(23, 7), date(22, 8))) {
            return Zodiac.LEO;
        }
        if (between(date, date(23, 8), date(22, 9))) {
            return Zodiac.VIRGO;
        }
        if (between(date, date(23, 9), date(22, 10))) {
            return Zodiac.LIBRA;
        }
        if (between(date, date(23, 10), date(22, 11))) {
            return Zodiac.SCORPIO;
        }
        if (between(date, date(23, 11), date(21, 12))) {
            return Zodiac.SAGITTARIUS;
        }
        if (between(date, date(22, 12), date(31, 12))
                || between(date, date(1, 1), date(19, 1))) {
            return Zodiac.CAPRICORN;
        }
        if (between(date, date(20, 1), date(18, 2))) {
            return Zodiac.AQUARIUS;
        }
        if (between(date, date(19, 2), date(20, 3))) {
            return Zodiac.PISCES;
        }

        throw new RuntimeException("Zodiac cannot be determined!");
    }

    public static Zodiac zodiac_rus(String birthDate) {
        LocalDate date = LocalDate.of(
                LocalDate.now().getYear(),
                birthMonth(birthDate),
                birthDay(birthDate));

        if (between(date, date(21, 3), date(19, 4))) {
            return Zodiac.ARIES;
        }
        if (between(date, date(20, 4), date(20, 5))) {
            return Zodiac.TAURUS;
        }
        if (between(date, date(21, 5), date(20, 6))) {
            return Zodiac.GEMINI;
        }
        if (between(date, date(21, 6), date(22, 7))) {
            return Zodiac.CANCER;
        }
        if (between(date, date(23, 7), date(22, 8))) {
            return Zodiac.LEO;
        }
        if (between(date, date(23, 8), date(22, 9))) {
            return Zodiac.VIRGO;
        }
        if (between(date, date(23, 9), date(22, 10))) {
            return Zodiac.LIBRA;
        }
        if (between(date, date(23, 10), date(21, 11))) {
            return Zodiac.SCORPIO;
        }
        if (between(date, date(22, 11), date(21, 12))) {
            return Zodiac.SAGITTARIUS;
        }
        if (between(date, date(22, 12), date(31, 12))
                || between(date, date(1, 1), date(19, 1))) {
            return Zodiac.CAPRICORN;
        }
        if (between(date, date(20, 1), date(18, 2))) {
            return Zodiac.AQUARIUS;
        }
        if (between(date, date(19, 2), date(20, 3))) {
            return Zodiac.PISCES;
        }

        throw new RuntimeException("Zodiac cannot be determined!");
    }

    /*public static Zodiac zodiacSign_rus_2(String birthDate) {
        LocalDate date = LocalDate.of(
                LocalDate.now().getYear(),
                birthMonth(birthDate),
                birthDay(birthDate));

        if (between(date, date(21, 3), date(20, 4))) {
            return Zodiac.ARIES;
        }
        if (between(date, date(21, 4), date(21, 5))) {
            return Zodiac.TAURUS;
        }
        if (between(date, date(22, 5), date(21, 6))) {
            return Zodiac.GEMINI;
        }
        if (between(date, date(22, 6), date(22, 7))) {
            return Zodiac.CANCER;
        }
        if (between(date, date(23, 7), date(21, 8))) {
            return Zodiac.LEO;
        }
        if (between(date, date(22, 8), date(23, 9))) {
            return Zodiac.VIRGO;
        }
        if (between(date, date(24, 9), date(23, 10))) {
            return Zodiac.LIBRA;
        }
        if (between(date, date(24, 10), date(22, 11))) {
            return Zodiac.SCORPIO;
        }
        if (between(date, date(23, 11), date(22, 12))) {
            return Zodiac.SAGITTARIUS;
        }
        if (between(date, date(23, 12), date(20, 1))) {
            return Zodiac.CAPRICORN;
        }
        if (between(date, date(21, 1), date(19, 2))) {
            return Zodiac.AQUARIUS;
        }
        if (between(date, date(20, 2), date(20, 3))) {
            return Zodiac.PISCES;
        }

        return null;
    }*/

    private static boolean between(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    private static LocalDate date(Integer day, Integer month) {
        int year = LocalDate.now().getYear();
        return LocalDate.of(year, month, day);
    }
}
