package com.kiselev.enemy.network.telegram.utils;

import lombok.SneakyThrows;

import java.util.Random;

public class TelegramUtils {

    public static String escapeMessage(String message) {
        return message
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\_", "\\\\_")
//                .replaceAll("\\[", "\\\\[")
//                .replaceAll("\\]", "\\\\]")
//                .replaceAll("\\(", "\\\\(")
//                .replaceAll("\\)", "\\\\)")
                .replaceAll("\\-", "\\\\-");
    }

    @SneakyThrows
    public static void timeout() {
        Thread.sleep(1000 + (long) (new Random().nextDouble() * (double) (2000 - 1000)));
    }
}
