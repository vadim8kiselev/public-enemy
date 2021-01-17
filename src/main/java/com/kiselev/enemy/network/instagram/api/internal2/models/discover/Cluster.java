package com.kiselev.enemy.network.instagram.api.internal2.models.discover;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import lombok.Data;

import java.util.List;

@Data
public class Cluster extends IGBaseModel {
    private String id;
    private String title;
    private String context;
    private String description;
    private List<String> labels;
    private String name;
    private String type;
}
