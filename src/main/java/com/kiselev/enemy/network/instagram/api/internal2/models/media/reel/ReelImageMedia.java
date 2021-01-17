package com.kiselev.enemy.network.instagram.api.internal2.models.media.reel;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;
import lombok.Data;

@Data
@JsonTypeName("1")
public class ReelImageMedia extends ReelMedia {
    private ImageVersions image_versions2;
}
