package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StoryHashtagItem extends StoryItem {
    private Hashtag hashtag;

    @Data
    public static class Hashtag {
        private String name;
        private long id;
    }
}
