package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class StatusResult {
    @NonNull
    private String status;
    private String message;

    private boolean spam;
    private boolean lock;
    private String feedback_title;
    private String feedback_message;
    private String error_type;
    private String checkpoint_url;

    public static void setValues(StatusResult to, StatusResult from) {
        to.setStatus(from.getStatus());
        to.setMessage(from.getMessage());
        to.setSpam(from.isSpam());
        to.setLock(from.isLock());
        to.setFeedback_title(from.getFeedback_title());
        to.setFeedback_message(from.getFeedback_message());
        to.setError_type(from.getError_type());
        to.setCheckpoint_url(from.getCheckpoint_url());
    }
}