package com.kiselev.enemy.network.vk.model;

public enum Zodiac {

    ARIES("♈️", "🔥", "Aries"),
    TAURUS("♉️", "🌍", "Taurus"),
    GEMINI("♊️", "💨", "Gemini"),
    CANCER("♋️", "💧", "Cancer"),
    LEO("♌️", "🔥", "Leo"),
    VIRGO("♍️", "🌍", "Virgo"),
    LIBRA("♎️", "💨", "Libra"),
    SCORPIO("♏️", "💧", "Scorpio"),
    SAGITTARIUS("♐️", "🔥", "Sagittarius"),
    CAPRICORN("♑️", "🌍", "Capricorn"),
    AQUARIUS("♒️", "💨", "Aquarius"),
    PISCES("♓️", "💧", "Pisces");

    private String sign;

    private String title;

    private String element;

    Zodiac(String sign, String element, String title) {
        this.sign = sign;
        this.element = element;
        this.title = title;
    }

    public String sign() {
        return sign;
    }

    public String element() {
        return element;
    }

    public String title() {
        return title;
    }
}
