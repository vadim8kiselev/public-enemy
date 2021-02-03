package com.kiselev.enemy.utils.flow.model;

import java.util.regex.Pattern;

public enum SocialNetwork {

    TG("[%s](https://t.me/%s)", Pattern.compile("^((https?://)?t\\.me/)(\\w+)$")),
    IG("[%s](https://instagram.com/%s)", Pattern.compile("^((https?://)?instagram\\.com/)(\\w+)$")),
    VK("[%s](https://vk.com/%s)", Pattern.compile("^((https?://)?vk\\.com/)(id\\d+|\\w+)$"));

    private final String template;

    private final Pattern pattern;

    SocialNetwork(String template, Pattern pattern) {
        this.template = template;
        this.pattern = pattern;
    }

    public String template() {
        return template;
    }

    public Pattern pattern() {
        return pattern;
    }
}
