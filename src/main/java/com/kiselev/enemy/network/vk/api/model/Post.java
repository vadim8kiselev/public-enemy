package com.kiselev.enemy.network.vk.api.model;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.utils.flow.model.Id;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.vk.api.sdk.objects.base.CommentsInfo;
import com.vk.api.sdk.objects.base.LikesInfo;
import com.vk.api.sdk.objects.base.RepostsInfo;
import com.vk.api.sdk.objects.wall.Geo;
import com.vk.api.sdk.objects.wall.Views;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class Post implements Id {

    private static final String POST_URL_TEMPLATE = "https://vk\\.com/id%s?w\\=wall%s\\_%s";

    @SerializedName("id")
    private String id;

    @SerializedName("owner_id")
    private Integer ownerId;

    @SerializedName("from_id")
    private String fromId;

    @SerializedName("date")
    private Integer date;

    @SerializedName("edited")
    private Integer edited;

    @SerializedName("geo")
    private Geo geo;

    @SerializedName("text")
    private String text;

//    @SerializedName("attachments")
//    private List<WallpostAttachment> attachments;

    @SerializedName("likes")
    private LikesInfo likesInfo;

    @SerializedName("reposts")
    private RepostsInfo reposts;

    @SerializedName("views")
    private Views views;

    @SerializedName("comments")
    private CommentsInfo commentsInfo;

    private transient List<VKProfile> likes;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return String.format(
                POST_URL_TEMPLATE,
                fromId,
                fromId,
                id
        );
    }
}
