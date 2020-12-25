package com.kiselev.enemy.network.instagram.api.internal2.models.media.thread;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.VideoVersionsMeta;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.thread.ThreadMedia;
import lombok.Data;

@Data
@JsonTypeName("2")
public class ThreadVideoMedia extends ThreadMedia {
    private ImageVersions image_versions2;
    private List<VideoVersionsMeta> video_versions;
}
