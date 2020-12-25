package com.kiselev.enemy.network.vk.api.model.full;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.wall.PostSource;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.Wallpost;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class PostFull extends Post {

    @SerializedName("access_key")
    private String accessKey;

    @SerializedName("is_archived")
    private Boolean isArchived;

    @SerializedName("is_favorite")
    private Boolean isFavorite;

    @SerializedName("post_source")
    private PostSource postSource;

    @SerializedName("post_type")
    private PostType postType;

    @SerializedName("signer_id")
    private Integer signerId;

    @SerializedName("copy_history")
    private List<Wallpost> copyHistory;

    @SerializedName("can_edit")
    private BoolInt canEdit;

    @SerializedName("created_by")
    private Integer createdBy;

    @SerializedName("can_delete")
    private BoolInt canDelete;

    @SerializedName("can_pin")
    private BoolInt canPin;

    @SerializedName("is_pinned")
    private Integer isPinned;

    @SerializedName("marked_as_ads")
    private BoolInt markedAsAds;
}
