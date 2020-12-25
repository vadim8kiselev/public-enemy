package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.utils.GsonHolder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FollowingResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<JsonObject> items;

    public List<Profile> profiles() {
        return items.stream()
                .filter(this::isProfile)
                .map(profile -> GsonHolder.GSON.fromJson(profile.toString(), Profile.class))
                .collect(Collectors.toList());
    }

    public List<Group> subscriptions() {
        return items.stream()
                .filter(this::isGroup)
                .map(group -> GsonHolder.GSON.fromJson(group.toString(), Group.class))
                .collect(Collectors.toList());
    }

    private boolean isProfile(JsonObject jsonObject) {
        if (jsonObject.has("type")) {
            String type = jsonObject.get("type").getAsString();
            return "profile".equals(type);
        }
        return false;
    }

    private boolean isGroup(JsonObject jsonObject) {
        if (jsonObject.has("type")) {
            String type = jsonObject.get("type").getAsString();
            return "page".equals(type);
        }
        return false;
    }
}

