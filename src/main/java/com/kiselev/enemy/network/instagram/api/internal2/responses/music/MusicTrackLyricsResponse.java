package com.kiselev.enemy.network.instagram.api.internal2.responses.music;

import com.kiselev.enemy.network.instagram.api.internal2.models.music.MusicLyrics;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

@Data
public class MusicTrackLyricsResponse extends IGResponse {
    private MusicLyrics lyrics;
}
