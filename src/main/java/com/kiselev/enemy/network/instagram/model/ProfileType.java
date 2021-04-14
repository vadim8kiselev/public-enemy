package com.kiselev.enemy.network.instagram.model;

public enum ProfileType {

    DEAD("Dead"),
    BOT("Bot"),
    VIEWER("Viewer"),
    NORMAL("Normal"),
    CREATOR("Creator"),
    BLOGGER("Blogger"),
    STAR("Star");

    private String value;

    ProfileType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
