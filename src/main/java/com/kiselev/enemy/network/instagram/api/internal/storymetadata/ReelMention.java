package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class ReelMention {
    /**
     * x location 0.0 - 1.0
     */
    @Builder.Default
    private double x = 0.5;
    /**
     * y location 0.0 - 1.0
     */
    @Builder.Default
    private double y = 0.5;
    /**
     * z location 0.0 - 1.0
     */
    @Builder.Default
    private double z = 0;
    /**
     * rotation 0.0 - 1.0
     */
    @Builder.Default
    private double rotation = 0;
    /**
     * user id (pk)
     */
    private String user_id;
    /**
     * height 0.0 - 1.0
     */
    @Builder.Default
    private double height = 1.0;
    /**
     * width 0.0 - 1.0
     */
    @Builder.Default
    private double width = 1.0;
    /**
     * unmodifiable values
     */
    private final boolean is_pinned = false;

    public Map<String, Object> map() {
        Map<String, Object> mapped = new HashMap<>();

        mapped.put("x", x);
        mapped.put("y", y);
        mapped.put("z", z);
        mapped.put("rotation", rotation);
        mapped.put("user_id", user_id);
        mapped.put("height", height);
        mapped.put("width", width);
        mapped.put("is_pinned", is_pinned);
        return mapped;
    }
}
