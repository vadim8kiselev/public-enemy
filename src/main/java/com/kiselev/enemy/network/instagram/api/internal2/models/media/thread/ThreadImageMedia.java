package com.kiselev.enemy.network.instagram.api.internal2.models.media.thread;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.thread.ThreadMedia;
import lombok.Data;

@Data
@JsonTypeName("1")
public class ThreadImageMedia extends ThreadMedia {
    private ImageVersions image_versions2;
}
