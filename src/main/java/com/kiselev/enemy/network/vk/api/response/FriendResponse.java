package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Profile;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class FriendResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<Profile> friends;

    public Integer getCount() {
        if (count == null) {
            count = 0;
        }
        return count;
    }

    public List<Profile> getFriends() {
        if (friends == null) {
            friends = Collections.emptyList();
        }
        return friends;
    }
}

