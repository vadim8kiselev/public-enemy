package com.kiselev.enemy.network.instagram.api.internal2.models.news;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import lombok.Data;

@Data
public class NewsStory extends IGBaseModel {
    private int type;
    private int story_type;
    private String pk;
}
