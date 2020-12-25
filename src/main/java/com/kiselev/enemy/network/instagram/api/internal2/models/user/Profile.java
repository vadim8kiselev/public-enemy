package com.kiselev.enemy.network.instagram.api.internal2.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Profile extends IGBaseModel {

    @EqualsAndHashCode.Include
    private Long pk;

    private String username;

    @JsonProperty("full_name")
    private String name;

    @JsonProperty("is_private")
    private boolean isPrivate;

    @JsonProperty("is_verified")
    private boolean isVerified;

    @JsonProperty("has_anonymous_profile_picture")
    private boolean hasAnonymousPhoto;

//    @JsonProperty("profile_pic_url")
//    private String photoUrl;
//
//    @JsonProperty("profile_pic_id")
//    private String photoId;

//          "account_badges": [],
//          "latest_reel_media": 1608831754,
//          "story_reel_media_ids": []
}
