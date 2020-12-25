package com.kiselev.enemy.network.instagram.model.internal.min;

import com.kiselev.enemy.network.instagram.model.InstagramPhoto;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.model.InstagramStory;
import lombok.Data;

import java.util.List;

@Data
public class InstagramProfileMin {

    private Long pk;
    private String username;
    private String fullName;

    private Integer mediaCount;
    private Integer followerCount;
    private Integer followingCount;
    private Integer geoMediaCount;
    private Integer userTagsCount;

    private String biography;
    private String category;
    private Float latitude;
    private Float longitude;

    private String externalUrl;

    private List<InstagramProfile> followers;
    private List<InstagramProfile> following;

    private List<InstagramPost> posts;

    private List<InstagramStory> stories;

    private InstagramPhoto mainPhoto;

    private Boolean isPrivate;
    private Boolean isVerified;
    private Boolean isBusiness;

    private String publicEmail;
    private String publicPhoneNumber;
    private String publicPhoneCountryCode;

    private String addressStreet;
    private String cityName;
    private String zip;

    private String directMessaging;
    private String businessContactMethod;

    private List<InstagramPhoto> photos;
}
