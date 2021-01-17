package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeName("profile")
public class ThreadProfileItem extends ThreadItem {
    private Profile profile;
    private List<TimelineMedia> preview_medias;
}
