package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Profile;
import lombok.Data;

import java.util.List;

@Data
public class LikeResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<Profile> likes;
}

