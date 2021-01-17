package com.kiselev.enemy.network.instagram.api.internal2.requests.music;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.music.MusicTrackLyricsResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MusicTrackLyricsRequest extends IGGetRequest<MusicTrackLyricsResponse> {
    @NonNull
    private String _track_id;

    @Override
    public String path() {
        return "music/track/" + _track_id + "/lyrics/";
    }

    @Override
    public Class<MusicTrackLyricsResponse> getResponseType() {
        return MusicTrackLyricsResponse.class;
    }

}
