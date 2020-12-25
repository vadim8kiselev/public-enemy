package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import lombok.Builder;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
public class Tally {
    /**
     * String to be displayed on tally
     */
    private String text;
    /**
     * This cannot be modified.
     */
    private final int count = 0;
    /**
     * Font size of text. Range from 17.5 to 35.0
     */
    @Builder.Default
    private double font_size = 35;
    /**
     * Predefined tallies YES and NO
     */
    public static Tally YES = Tally.builder().text("YES").build(), NO = Tally.builder().text("NO").build();

    public Map<String, Object> map() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("count", count);
        map.put("text", text);
        map.put("font_size", font_size > 35 ? 35 : font_size < 17.5 ? 17.5 : font_size);

        return map;
    }
}
