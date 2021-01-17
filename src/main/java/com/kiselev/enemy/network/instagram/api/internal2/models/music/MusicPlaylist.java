package com.kiselev.enemy.network.instagram.api.internal2.models.music;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MusicPlaylist extends IGBaseModel {
    private String id;
    private String title;
    private String icon_url;
    @JsonDeserialize(converter = BeanToTrackConverter.class)
    private List<com.kiselev.enemy.network.instagram.api.internal2.models.music.MusicTrack> preview_items;

    public static class BeanToTrackConverter
            extends StdConverter<List<Map<String, Object>>, List<com.kiselev.enemy.network.instagram.api.internal2.models.music.MusicTrack>> {
        @Override
        public List<com.kiselev.enemy.network.instagram.api.internal2.models.music.MusicTrack> convert(List<Map<String, Object>> value) {
            return value.stream()
                    .filter(m -> m.containsKey("track"))
                    .map(m -> IGUtils.convertToView(m.get("track"), MusicTrack.class))
                    .collect(Collectors.toList());
        }
    }
}
