package com.kiselev.enemy.network.instagram.model.internal.min;

import lombok.Data;

@Data
public class InstagramCommentaryMin {

    private Long pk;

    private Long userId;

    private Long mediaId;

    private Integer type;

    private String text;

    private String timestamp;

    private Integer commentLikeCount;

    private String contentType;

    private String status;
}
