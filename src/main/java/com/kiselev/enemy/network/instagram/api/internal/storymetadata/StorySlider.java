package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StorySlider
 * @author Justin Vo
 *
 */
@Builder
public class StorySlider extends StoryMetadata {
    /**
     * width 0.0 - 1.0
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
     * Question to display
     */
    private String question;
    /**
     * Emoji
     */
    @Builder.Default
    private String emoji = "\uD83D\uDE0D";
    /**
     * Color code in HEX
     */
    @Builder.Default
    private String text_color = "#7F007F";
    /**
     * Color code in HEX
     */
    @Builder.Default
    private String background_color = "#FFFFFF";
    /**
     * Unmodifiable
     */
    private final boolean is_pinned = false;
    private final boolean is_hidden = false;

    @Override
    public String key() {
        // TODO Auto-generated method stub
        return "story_sliders";
    }

    public List<Object> map() {
        Map<String, Object> storyslider = new HashMap<>();

        storyslider.put("x", x);
        storyslider.put("y", y);
        storyslider.put("z", z);
        storyslider.put("rotation", rotation);
        storyslider.put("height", height);
        storyslider.put("width", width);
        storyslider.put("is_pinned", is_pinned);
        storyslider.put("is_hidden", is_hidden);
        storyslider.put("text_color", text_color);
        storyslider.put("emoji", emoji);
        storyslider.put("question", question);
        storyslider.put("background_color", background_color);

        return Arrays.asList(storyslider);
    }

    @Override
    @SneakyThrows
    public String metadata() {
        return new ObjectMapper().writeValueAsString(this.map());
    }

    @Override
    public boolean check() throws IllegalArgumentException {
        return true;
    }

}
