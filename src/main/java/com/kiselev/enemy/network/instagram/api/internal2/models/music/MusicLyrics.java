package com.kiselev.enemy.network.instagram.api.internal2.models.music;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import lombok.Data;

import java.util.List;

@Data
public class MusicLyrics {
    private List<LyricPhrase> phrases;

    @Data
    public static class LyricPhrase extends IGBaseModel {
        private long start_time_in_ms;
        private long end_time_in_ms;
        private String phrase;
    }
}
