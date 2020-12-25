package com.kiselev.enemy.network.instagram.model;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramComment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstagramCommentary {

    private String id;

    private Long userId;

    private String text;

    private String date;

    private Integer likesCount;

    public InstagramCommentary(InstagramComment commentary) {
        this.id = commentary.getPk();

        this.userId = commentary.getUser_id();

        this.text = commentary.getText();

        this.date = InstagramUtils.dateAndTime(commentary.getCreated_at_utc());

        this.likesCount = commentary.getComment_like_count();
    }

    public InstagramCommentary(Comment commentary) {
        this.id = commentary.getPk();

        this.userId = commentary.getUser_id();

        this.text = commentary.getText();

        this.date = InstagramUtils.dateAndTime(commentary.getCreated_at_utc());

        this.likesCount = commentary.getComment_like_count();

    }
}
