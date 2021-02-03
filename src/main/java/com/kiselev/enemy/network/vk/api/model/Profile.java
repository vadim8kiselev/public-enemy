package com.kiselev.enemy.network.vk.api.model;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.utils.flow.model.Info;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.base.Sex;
import com.vk.api.sdk.objects.users.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile implements Info {

    @EqualsAndHashCode.Include
    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("sex")
    private Sex sex;

    @SerializedName("screen_name")
    private String username;

    @SerializedName("domain")
    private String domain;

    @SerializedName("bdate")
    private String birthDate;

    @SerializedName("country")
    private BaseObject country;

    @SerializedName("city")
    private BaseObject city;

    @SerializedName("home_town")
    private String homeTown;

    @SerializedName("mobile_phone")
    private String mobilePhone;

    @SerializedName("home_phone")
    private String homePhone;

    @SerializedName("site")
    private String site;

    @SerializedName("instagram")
    private String instagram;

    @SerializedName("facebook")
    private String facebook;

    @SerializedName("facebook_name")
    private String facebookName;

    @SerializedName("twitter")
    private String twitter;

    @SerializedName("skype")
    private String skype;

    @SerializedName("status_audio")
    private Audio statusAudio;

    @SerializedName("status")
    private String status;

    @SerializedName("activity")
    private String activity;

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

    @SerializedName("relation")
    private UserRelation relation;

    @SerializedName("relation_partner")
    private UserMin relationPartner;

    //@SerializedName("personal")
    //private Personal personal;

    @SerializedName("universities")
    private List<University> universities;

    @SerializedName("schools")
    private List<School> schools;

    @SerializedName("relatives")
    private List<Relative> relatives;

    @SerializedName("counters")
    private UserCounters counters;

    @SerializedName("deactivated")
    private String deactivated;

    @SerializedName("is_closed")
    private String isPrivate;

    @SerializedName("is_friend")
    private BoolInt isFriend;

    @SerializedName("photo_max")
    private URL photo;

    public Sex sex() {
        if (this.sex == null) {
            this.sex = Sex.UNKNOWN;
        }
        return this.sex;
    }

    public String fullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String name() {
        return String.format(
                type().template(),
                firstName + " " + lastName,
                username != null ? username : ("id" + id)
        );
    }

    @Override
    public SocialNetwork type() {
        return SocialNetwork.VK;
    }
}
