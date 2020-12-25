package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Photo;
import lombok.Data;

import java.util.List;

@Data
public class PhotoAllResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<Photo> photos;
}

