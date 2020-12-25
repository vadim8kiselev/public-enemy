package com.kiselev.enemy.network.vk.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class VKUtils {

    public static void timeout() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static String date(String rawDate) {
        if (rawDate != null && rawDate.split("\\.").length == 3) {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("d.M.yyyy");
            LocalDate birthDate = LocalDate.parse(rawDate, pattern);
            return String.valueOf(
                    Period.between(birthDate, LocalDate.now()).getYears()
            );
        }
        // TODO: parse for two values
        return null;
    }
}
