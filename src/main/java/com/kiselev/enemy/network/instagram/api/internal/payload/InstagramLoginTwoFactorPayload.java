package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstagramLoginTwoFactorPayload {
    private String username;
    private String phone_id;
    private String _csrftoken;
    private String guid;
    private String device_id;
    private String verification_code;
    private String two_factor_identifier;
    private String password;
    @Builder.Default
    private int login_attempt_account = 0;

}