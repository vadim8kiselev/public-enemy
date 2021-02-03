package com.kiselev.enemy.network.instagram.api.internal2.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Profile {

    private List<ProfilePic> hd_profile_pic_versions;

    private ProfilePic hd_profile_pic_url_info;

    private String biography;
//    private String biography_with_entities;

    private String external_url;

    private String category;

    @JsonProperty("public_email")
    private String email;

    @JsonProperty("public_phone_number")
    private String publicPhoneNumber;

    @JsonProperty("contact_phone_number")
    private String contactPhoneNumber;

    @JsonProperty("city_name")
    private String address;

    @JsonProperty("is_business")
    private boolean isBusiness;

    @JsonProperty("is_favorite")
    private boolean isFavorite;

    @JsonProperty("media_count")
    private Integer mediaCount;

    @JsonProperty("follower_count")
    private Integer followerCount;

    @JsonProperty("following_count")
    private Integer followingCount;

    @Data
    public static class ProfilePic {
        public URL url;
        public int width;
        public int height;
    }


//            "public_email": "vadim8kiselev@gmail.com",
//            "public_phone_number": "",

//            "media_count": 89,
//            "follower_count": 112,
//            "following_count": 30,
//            "geo_media_count": 0,
//            "following_tag_count": 0,
//            "total_igtv_videos": 0,
//            "usertags_count": 4,
//            "mutual_followers_count": 0,

//            "is_favorite": false,
//            "is_favorite_for_stories": false,
//            "is_favorite_for_igtv": false,
//            "live_subscription_status": "default",
//            "has_chaining": true,
//
//            "address_street": "",
//            "business_contact_method": "",
//            "category": "Personal Blog",

//            "city_id": 0,
//            "city_name": "",
//            "contact_phone_number": "",

//            "is_call_to_action_enabled": false,
//            "latitude": 0.0,
//            "longitude": 0.0,
//            "public_phone_country_code": "",
//            "zip": "",

//            "instagram_location_id": "",
//            "professional_conversion_suggested_account_type": 2,

//            "can_hide_category": true,
//            "can_hide_public_contacts": true,
//            "should_show_category": true,
//            "should_show_public_contacts": true,

//            "account_badges": []


    public static User deleted(String username) {
        User deletedUser = new User();
        deletedUser.id(String.valueOf(username.hashCode()));
        deletedUser.username(username);
        deletedUser.isDeleted(true);
        return deletedUser;
    }
}
