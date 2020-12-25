package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramLoginResult extends StatusResult {
    private InstagramLoggedUser logged_in_user;
    private InstagramTwoFactorInfo two_factor_info;
    private InstagramChallenge challenge;

}