package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import lombok.Data;

@Data
public class StoryItem {
    private double x;
    private double y;
    private double z;
    private double width;
    private double height;
    private double rotation;
    private int is_pinned;
    private int is_hidden;
    private int is_sticker;
}
