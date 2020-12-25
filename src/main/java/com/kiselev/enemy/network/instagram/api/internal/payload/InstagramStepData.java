package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * InstagramStepData.
 *
 * @author evosystem
 */
@Getter
@Setter
@ToString
public class InstagramStepData {

    private int choice;
    private String fb_access_token;
    private String big_blue_token;
    private boolean google_oauth_token;
    private String phone_number;
    private String email;
    private String security_code;
    private int resend_delay;
    private int sms_resend_delay;
    private String contact_point;
    private String form_type;
    private String phone_number_preview;
    private String country;
    private long enrollment_time;
    private String enrollment_date;
    private double latitude;
    private double longitude;
    private String city;
    private String platform;
    private String user_agent;
}