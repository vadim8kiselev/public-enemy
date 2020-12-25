package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;

import java.util.List;


@Data
public class InstagramCurrentUserProfile {

    private long pk;
    private String username;
    private String full_name;
    private boolean is_private;
    private String profile_pic_url;
    private boolean is_verified;
    private boolean has_anonymous_profile_picture;
    private String biography;
    private String external_url;

    /**
     * Auto archive stories for viewing them later.
     * It will appear in your archive once it has disappeared from your story feed.
     * Possible values: "unset" (initial value), "on", "off"
     */
    private String reel_auto_archive;
    private List<InstagramProfilePic> hd_profile_pic_versions;
    private InstagramProfilePic hd_profile_pic_url_info;
    private boolean show_conversion_edit_entry;

    /**
     * Possible values: "any", ...
     */
    private String allowed_commenter_type;

    private String birthday;

    private String phone_number;
    private String country_code;
    private Long national_number;

    private InstagramUserGenderEnum gender;
    private String email;
    private boolean can_link_entities_in_bio;

}
