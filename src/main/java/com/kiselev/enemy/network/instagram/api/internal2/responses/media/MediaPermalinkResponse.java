package com.kiselev.enemy.network.instagram.api.internal2.responses.media;

import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

@Data
public class MediaPermalinkResponse extends IGResponse {
    private String permalink;
}
