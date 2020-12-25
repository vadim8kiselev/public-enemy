package com.kiselev.enemy.network.instagram.api.internal2.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;

import lombok.Data;

@Data
public class IGResponse extends IGBaseModel {
    private String status;
    @JsonIgnore
    private int statusCode;
    private String message;
    private boolean spam;
    private boolean lock;
    private String feedback_title;
    private String feedback_message;
    private String error_type;
    private String checkpoint_url;
}
