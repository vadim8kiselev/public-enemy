package com.kiselev.enemy.network.vk.api.response;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import lombok.Data;

import java.util.List;

@Data
public class ConversationsResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("unread_count")
    private Integer unreadCount;

    @SerializedName("items")
    private List<ConversationWithMessage> items;

    @SerializedName("profiles")
    private List<Profile> profiles;

    @SerializedName("groups")
    private List<Group> groups;
}
