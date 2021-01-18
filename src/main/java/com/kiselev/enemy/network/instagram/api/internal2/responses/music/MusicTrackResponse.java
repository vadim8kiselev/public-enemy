package com.kiselev.enemy.network.instagram.api.internal2.responses.music;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kiselev.enemy.network.instagram.api.internal2.models.music.MusicPlaylist.BeanToTrackConverter;
import com.kiselev.enemy.network.instagram.api.internal2.models.music.MusicTrack;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;
import lombok.Data;

import java.util.List;

@Data
public class MusicTrackResponse extends IGPaginatedResponse {
    @JsonDeserialize(converter = BeanToTrackConverter.class)
    private List<MusicTrack> items;
    private MusicTrackPageInfo page_info;

    public String getNext_max_id() {
        return page_info.getNext_max_id();
    }

    public boolean isMore_available() {
        return page_info.isMore_available();
    }

    @Data
    public static class MusicTrackPageInfo {
        private String next_max_id;
        private boolean more_available;
    }
}
