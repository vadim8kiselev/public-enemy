package com.kiselev.enemy.network.instagram.api.internal.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Alexander Kohonovsky
 * @since 2019-04-28
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramGetCurrentUserProfileResult extends StatusResult {

    private InstagramCurrentUserProfile user;

    /**
     * Can be a nested object or string
     */
    @JsonProperty("message")
    private Object messages;

}
