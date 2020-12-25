package com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.item.ReelMetadataItem;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class StoryLocationsItem extends ReelMetadataItem {
    @NonNull
    private String location_id;

    @Override
    public String key() {
        return "story_locations";
    }

}
