package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StoryQuestion
 *
 * @author Justin Vo
 *
 */
@Builder
public class StoryQuestion extends StoryMetadata {
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
     * Color of background
     */
    @Builder.Default
    private String background_color = "#FFFFFF";
    /**
     * Story Question displayed
     */
    private String question;
    /**
     * Profile Pic URL of the account
     */
    private String profile_pic_url;
    /**
     * These are constants and should not be changed
     */
    private final boolean viewer_can_interact = false;
    private final boolean is_sticker = true;
    private final String question_type = "text";
    private final double z = 0;

    private List<Object> map() {
        Map<Object, Object> story_question = new HashMap<>();

        story_question.put("x", x);
        story_question.put("y", y);
        story_question.put("z", z);
        story_question.put("rotation", rotation);
        story_question.put("height", height);
        story_question.put("width", width);
        story_question.put("text_color", text_color);
        story_question.put("background_color", background_color);
        story_question.put("question", question);
        story_question.put("profile_pic_url", profile_pic_url);
        story_question.put("is_sticker", is_sticker);
        story_question.put("viewer_can_interact", viewer_can_interact);
        story_question.put("question_type", question_type);

        return Arrays.asList(story_question);
    }

    @Override
    public String key() {
        return "story_questions";
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
