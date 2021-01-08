package com.kiselev.enemy.network.vk.utils;

import com.vk.api.sdk.objects.base.BaseObject;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class VKUtils {

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

    public static Integer age(String rawDate) {
        if (rawDate != null && rawDate.split("\\.").length == 3) {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("d.M.yyyy");
            LocalDate birthDate = LocalDate.parse(rawDate, pattern);
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return null;
    }
}
