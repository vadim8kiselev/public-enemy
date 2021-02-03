package com.kiselev.enemy.network.vk.api.model;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.utils.flow.model.Id;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class Photo implements Id {

    @SerializedName("id")
    private String id;

    @SerializedName("album_id")
    private Integer albumId;

    @SerializedName("date")
    private Integer date;

    @SerializedName("lat")
    private Float lat;

    @SerializedName("long")
    private Float lng;

    @SerializedName("owner_id")
    private Integer ownerId;

    @SerializedName("text")
    private String text;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("height")
    private Integer height;

    @SerializedName("width")
    private Integer width;

    @ToString.Exclude
    private transient List<VKProfile> likes;

    @Override
    public String name() {
        return null; // TODO: url
    }
}
