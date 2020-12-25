package com.kiselev.enemy.network.instagram.api.internal2.responses.feed;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.feed.Reel;
import com.kiselev.enemy.network.instagram.api.internal2.models.live.Broadcast;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class FeedReelsTrayResponse extends IGResponse {
    private List<Reel> tray;
    private List<Broadcast> broadcasts;
}
