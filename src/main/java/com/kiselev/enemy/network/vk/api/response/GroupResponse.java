package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Group;
import lombok.Data;

import java.util.List;

@Data
public class GroupResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<Group> groups;
}

