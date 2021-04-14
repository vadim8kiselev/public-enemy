package com.kiselev.enemy.network.vk.model;

import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.utils.flow.model.Id;
import com.vk.api.sdk.objects.base.CommentsInfo;
import com.vk.api.sdk.objects.base.LikesInfo;
import com.vk.api.sdk.objects.base.Place;
import com.vk.api.sdk.objects.base.RepostsInfo;
import com.vk.api.sdk.objects.wall.Geo;
import com.vk.api.sdk.objects.wall.Views;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VKPost implements Id {

    private static final String POST_URL_TEMPLATE = "https://vk\\.com/id%s?w\\=wall%s\\_%s";

    private String id;

    private Integer ownerId;

    private String fromId;

    private LocalDateTime date;

    private String location;

    private String text;

//    private List<WallpostAttachment> attachments;

    private LikesInfo likesInfo;

    private RepostsInfo reposts;

    private Views views;

    private CommentsInfo commentsInfo;

    private transient List<VKProfile> likes;

    public VKPost(Post post) {
        this.id = post.id();
        this.ownerId = post.ownerId();
        this.fromId = post.fromId();
        this.date = VKUtils.timestampToDateAndTime(post.date());
        this.location = Optional.ofNullable(post.geo())
                .map(Geo::getPlace)
                .map(Place::getAddress)
                .orElse(null);
        this.text = post.text();
        this.likesInfo = post.likesInfo();
        this.reposts = post.reposts();
        this.views = post.views();
        this.commentsInfo = post.commentsInfo();
        this.likes = post.likes();
    }

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
