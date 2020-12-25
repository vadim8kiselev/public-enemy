package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramLikeResult extends StatusResult {
    private boolean spam;
    private String feedback_ignore_label;
    private String feedback_title;
    private String feedback_message;
    private String feedback_url;
    private String feedback_action;
    private String feedback_appeal_label;


}