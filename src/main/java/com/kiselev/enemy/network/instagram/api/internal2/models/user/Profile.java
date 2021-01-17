package com.kiselev.enemy.network.instagram.api.internal2.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.utils.flow.model.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Profile extends IGBaseModel implements Id {

    @JsonProperty("pk")
    @EqualsAndHashCode.Include
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("is_private")
    private boolean isPrivate;

    private boolean isDeleted;

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

    @Override
    public String name() {
        return username;
    }
}
