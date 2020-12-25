package com.kiselev.enemy.network.vk.api.model.full;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.base.Sex;
import com.vk.api.sdk.objects.friends.FriendStatusStatus;
import com.vk.api.sdk.objects.friends.RequestsMutual;
import com.vk.api.sdk.objects.users.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class ProfileFull extends Profile {

    @SerializedName("hidden")
    private Integer hidden;

    @SerializedName("can_access_closed")
    private Boolean canAccessClosed;

    @SerializedName("is_closed")
    private Boolean isClosed;





    //@SerializedName("online")
    //private BoolInt online;

    //@SerializedName("online_mobile")
    //private BoolInt onlineMobile;

    //@SerializedName("online_app")
    //private Integer onlineApp;

    //@SerializedName("verified")
    //private BoolInt verified;

    @SerializedName("trending")
    private BoolInt trending;

    @SerializedName("friend_status")
    private FriendStatusStatus friendStatus;

    @SerializedName("mutual")
    private RequestsMutual mutual;






    @SerializedName("nickname")
    private String nickname;

    @SerializedName("maiden_name")
    private String maidenName;

    @SerializedName("timezone")
    private Integer timezone;

    @SerializedName("photo_200")
    private URL photo200;

    @SerializedName("photo_max")
    private URL photoMax;

    @SerializedName("photo_200_orig")
    private URL photo200Orig;

    @SerializedName("photo_400_orig")
    private URL photo400Orig;

    @SerializedName("photo_max_orig")
    private URL photoMaxOrig;

    @SerializedName("photo_id")
    private String photoId;

    @SerializedName("has_photo")
    private BoolInt hasPhoto;

    @SerializedName("has_mobile")
    private BoolInt hasMobile;

    @SerializedName("wall_comments")
    private BoolInt wallComments;

    @SerializedName("can_post")
    private BoolInt canPost;

    @SerializedName("can_see_all_posts")
    private BoolInt canSeeAllPosts;

    @SerializedName("can_see_audio")
    private BoolInt canSeeAudio;

    @SerializedName("can_write_private_message")
    private BoolInt canWritePrivateMessage;

    @SerializedName("can_send_friend_request")
    private BoolInt canSendFriendRequest;

    @SerializedName("mobile_phone")
    private String mobilePhone;

    @SerializedName("home_phone")
    private String homePhone;

    @SerializedName("site")
    private String site;

    @SerializedName("status_audio")
    private Audio statusAudio;

    @SerializedName("status")
    private String status;

    @SerializedName("activity")
    private String activity;

    //@SerializedName("last_seen")
    //private LastSeen lastSeen;

    //@SerializedName("exports")
    //private Exports exports;

    @SerializedName("crop_photo")
    private CropPhoto cropPhoto;

    @SerializedName("followers_count")
    private Integer followersCount;

    @SerializedName("blacklisted")
    private BoolInt blacklisted;

    @SerializedName("blacklisted_by_me")
    private BoolInt blacklistedByMe;

    @SerializedName("is_favorite")
    private BoolInt isFavorite;

    @SerializedName("is_hidden_from_feed")
    private BoolInt isHiddenFromFeed;

    @SerializedName("common_count")
    private Integer commonCount;

    @SerializedName("occupation")
    private Occupation occupation;

    @SerializedName("career")
    private List<Career> career;

    @SerializedName("military")
    private List<Military> military;

    @SerializedName("university")
    private Integer university;

    @SerializedName("university_name")
    private String universityName;

    @SerializedName("faculty")
    private Integer faculty;

    @SerializedName("faculty_name")
    private String facultyName;

    @SerializedName("graduation")
    private Integer graduation;

    @SerializedName("education_form")
    private String educationForm;

    @SerializedName("education_status")
    private String educationStatus;

    @SerializedName("home_town")
    private String homeTown;

    @SerializedName("relation")
    private UserRelation relation;

    @SerializedName("relation_partner")
    private UserMin relationPartner;

//    @SerializedName("personal")
////    private Personal personal;

    @SerializedName("universities")
    private List<University> universities;

    @SerializedName("schools")
    private List<School> schools;

    @SerializedName("relatives")
    private List<Relative> relatives;

    @SerializedName("is_subscribed_podcasts")
    private Boolean isSubscribedPodcasts;

    @SerializedName("can_subscribe_podcasts")
    private Boolean canSubscribePodcasts;

    @SerializedName("can_subscribe_posts")
    private Boolean canSubscribePosts;


    @SerializedName("counters")
    private UserCounters counters;
}
