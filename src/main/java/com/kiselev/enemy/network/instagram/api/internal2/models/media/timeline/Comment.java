package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import lombok.Data;

@Data
public class Comment extends IGBaseModel {
    private String pk;
    private long user_id;
    private String text;
    private int type;
    private long created_at;
    private long created_at_utc;
    private String content_type;
    private String status;
    private int bit_flags;
    private User user;
    private boolean did_report_as_spam;
    private boolean share_enabled;
    private long media_id;
    private int comment_like_count;

    @Data
    public static class Caption extends Comment {

    }
}
