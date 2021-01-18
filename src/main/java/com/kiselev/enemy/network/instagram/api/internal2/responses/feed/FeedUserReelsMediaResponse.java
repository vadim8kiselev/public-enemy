package com.kiselev.enemy.network.instagram.api.internal2.responses.feed;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kiselev.enemy.network.instagram.api.internal2.models.feed.Reel;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

@Data
public class FeedUserReelsMediaResponse extends IGResponse {
    @JsonUnwrapped
    private Reel reel;
}
