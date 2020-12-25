package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class StoryCountdown extends StoryMetadata {
    /**
     * x position of sticker. Range from 0.0 to 1.0. 0.5 is center
     */
    @Builder.Default
    private double x = 0.5;
    /**
     * y position of sticker. Range from 0.0 to 1.0. 0.5 is center
     */
    @Builder.Default
    private double y = 0.5;
    /**
     * width of sticker. Range from 0 to 1.0
     */
    @Builder.Default
    private double width = 0.5;
    /**
     * height of sticker. Range from 0 to 1.0
     */
    @Builder.Default
    private double height = 0.5;
    /**
     * Rotation of sticker
     */
    @Builder.Default
    private double rotation = 0;
    /**
     * Color of text
     */
    @Builder.Default
    private String text_color = "#000000";
    /**
     * Color of Start display
     */
    @Builder.Default
    private String start_background_color = "#CA2EE1";
    /**
     * Color of End display
     */
    @Builder.Default
    private String end_background_color = "#5EB1FF";
    /**
     * Color of digits
     */
    @Builder.Default
    private String digit_color = "#7E0091";
    /**
     * Color of digit card
     */
    @Builder.Default
    private String digit_card_color = "#FFFFFF";
    /**
     * Allow viewers to subscribe to a notification when the countdown ends
     */
    @Builder.Default
    private boolean following_enabled = true;
    /**
     * Text display
     */
    private String text;
    /**
     * UNIX Epoch of when countdown ends in seconds
     */
    private long end_ts;
    /**
     * These are constants and should not be changed
     */
    private final boolean is_sticker = true;
    private final double z = 0;

    private List<Map<Object, Object>> map() {
        Map<Object, Object> story_countdown = new HashMap<>();

        story_countdown.put("x", x);
        story_countdown.put("y", y);
        story_countdown.put("z", z);
        story_countdown.put("rotation", rotation);
        story_countdown.put("height", height);
        story_countdown.put("width", width);
        story_countdown.put("text_color", text_color);
        story_countdown.put("text", text);
        story_countdown.put("is_sticker", is_sticker);
        story_countdown.put("start_background_color", start_background_color);
        story_countdown.put("end_background_color", end_background_color);
        story_countdown.put("end_ts", end_ts);
        story_countdown.put("following_enabled", following_enabled);
        story_countdown.put("digit_color", digit_color);
        story_countdown.put("digit_card_color", digit_card_color);
        return Arrays.asList(story_countdown);
    }

    @Override
    public String key() {
        return "story_countdowns";
    }

    @Override
    @SneakyThrows
    public String metadata() {
        return new ObjectMapper().writeValueAsString(map());
    }

    @Override
    public boolean check() throws IllegalArgumentException {
        return true;
    }

}
