package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * InstagramSelectVerifyMethodResult.
 *
 * @author evosystem
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramSelectVerifyMethodResult extends StatusResult {

    private String action;
    private String step_name;
    private InstagramStepData step_data;
    private long user_id;
    private String nonce_code;
}