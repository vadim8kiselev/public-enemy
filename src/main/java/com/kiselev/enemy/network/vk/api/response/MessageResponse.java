package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.vk.api.sdk.objects.messages.Message;
import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<Message> messages;

    @SerializedName("profiles")
    private List<Profile> profiles;

    @SerializedName("groups")
    private List<Group> groups;
}

