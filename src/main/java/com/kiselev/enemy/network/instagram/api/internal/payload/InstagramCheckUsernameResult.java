package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramCheckUsernameResult extends StatusResult {

    private boolean available;
    private String username;
    private String error;
    private String error_type;

}
