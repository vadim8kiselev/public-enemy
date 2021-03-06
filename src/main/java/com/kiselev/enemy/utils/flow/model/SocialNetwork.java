package com.kiselev.enemy.utils.flow.model;

import java.util.regex.Pattern;

public enum SocialNetwork {

    TG("[%s](https://t.me/%s)", Pattern.compile("^((https?://)?t\\.me/)(\\S+)$")),
    IG("[%s](https://instagram.com/%s)", Pattern.compile("^((https?://)?instagram\\.com/)(\\S+)$")),
    VK("[%s](https://vk.com/%s)", Pattern.compile("^((https?://)?vk\\.com/)(id\\d+|\\S+)$")),
    FB("[%s](https://facebook.com/%s)", null),
    TW("[%s](https://twitter.com/%s)", null),
    SK("", null);

    private final String template;

    private final Pattern pattern;

    SocialNetwork(String template, Pattern pattern) {
        this.template = template;
        this.pattern = pattern;
    }

    public String template() {
        return template;
    }

    public String link(String name, String username) {
        return String.format(
                template,
                name,
                username);
    }

    public Pattern pattern() {
        return pattern;
    }
}
