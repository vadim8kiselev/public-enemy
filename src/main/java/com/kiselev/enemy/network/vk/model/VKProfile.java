package com.kiselev.enemy.network.vk.model;

import com.kiselev.enemy.network.vk.api.model.Group;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.annotation.EnemyValue;
import com.kiselev.enemy.utils.flow.annotation.EnemyValues;
import com.kiselev.enemy.utils.flow.model.Info;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.users.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kiselev.enemy.network.vk.model.constants.VKMessages.*;

@Data
@Document
@NoArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VKProfile implements Info {

    @ToString.Exclude
    private Profile profile;

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @EnemyValue(message = SCREEN_NAME_MESSAGE)
    private String username;

    @EnemyValue(message = FIRST_NAME_MESSAGE)
    private String firstName;

    @EnemyValue(message = LAST_NAME_MESSAGE)
    private String lastName;

    @EnemyValue
    private String fullName;

    @EnemyValue
    private String status;

    @EnemyValue(message = SEX_MESSAGE)
    private String sex;

    @EnemyValue(message = AGE_MESSAGE)
    private String age;

    @EnemyValue(message = BIRTHDAY_MESSAGE)
    private String birthDate;

    @EnemyValue
    private Integer birthDay;

    @EnemyValue
    private Integer birthMonth;

    @EnemyValue
    private Integer birthYear;

    private URL photo;

    @EnemyValue(message = COUNTRY_MESSAGE)
    private String country;

    @EnemyValue(message = CITY_MESSAGE)
    private String city;

    private String countryCode;

    private String cityCode;

    @EnemyValue(message = HOME_TOWN_MESSAGE)
    private String homeTown;

    @EnemyValue
    private String phone;

    @EnemyValue
    private String site;

    @EnemyValue
    private String facebook;

    @EnemyValue
    private String twitter;

    @EnemyValue
    private String skype;

    @EnemyValue
    private String instagram;

    @EnemyValue
    private String telegram;

    @EnemyValue
    private List<String> university;

    @EnemyValue
    private List<String> school;

    @EnemyValue
    private List<String> job;

    private boolean isFriend;

    private boolean isPrivate;

    private boolean isDeactivated;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_PHOTO_MESSAGE, deleteMessage = DELETED_PHOTO_MESSAGE)
    private List<Photo> photos;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_FRIEND_MESSAGE, deleteMessage = DELETED_FRIEND_MESSAGE)
    private List<VKProfile> friends;

    @ToString.Exclude
    @EnemyValues
    private Map<VKProfile, List<VKProfile>> area;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_HIDDEN_FRIEND_MESSAGE, deleteMessage = DELETED_HIDDEN_FRIEND_MESSAGE)
    private List<VKProfile> hiddenFriends;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_FOLLOWER_MESSAGE, deleteMessage = DELETED_FOLLOWER_MESSAGE)
    private List<VKProfile> followers;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_FOLLOWING_MESSAGE, deleteMessage = DELETED_FOLLOWING_MESSAGE)
    private List<VKProfile> following;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_GROUP_MESSAGE, deleteMessage = DELETED_GROUP_MESSAGE)
    private List<Group> communities;

    @ToString.Exclude
    @EnemyValues(newMessage = NEW_POST_MESSAGE, deleteMessage = DELETED_POST_MESSAGE)
    private List<Post> posts;

    @ToString.Exclude
    private List<VKProfile> relatives;

    @ToString.Exclude
    private List<VKProfile> likes;

    private LocalDateTime timestamp;

    public VKProfile(Profile profile) {
        this.timestamp = LocalDateTime.now();
        this.profile = profile;

        this.id = profile.id();
        this.username = profile.username();
        this.firstName = profile.firstName();
        this.lastName = profile.lastName();
        this.fullName = profile.firstName() + " " + profile.lastName();
        this.status = profile.status();
        switch (profile.sex()) {
            case MALE: this.sex = "Male"; break;
            case FEMALE: this.sex = "Female"; break;
            case UNKNOWN: this.sex = "Unknown"; break;
        }
        this.age = VKUtils.age(profile.birthDate());
        this.birthDate = profile.birthDate();
        this.birthDay = VKUtils.birthDay(profile.birthDate());
        this.birthMonth = VKUtils.birthMonth(profile.birthDate());
        this.birthYear = VKUtils.birthYear(profile.birthDate());
        this.photo = profile.photo();
        this.country = VKUtils.title(profile.country());
        this.city = VKUtils.title(profile.city());
        this.countryCode = VKUtils.code(profile.country());
        this.cityCode = VKUtils.code(profile.city());
        this.homeTown = StringUtils.isNotEmpty(profile.homeTown()) ? profile.homeTown() : null;
        this.phone = ObjectUtils.firstNonNull(profile.mobilePhone(), profile.homePhone());
        this.site = profile.site();
        this.facebook = profile.facebook();
        this.twitter = profile.twitter();
        this.skype = profile.skype();
        this.instagram = ObjectUtils.firstNonNull(
                profile.instagram(),
                ProfilingUtils.identifier(SocialNetwork.IG, profile.site()),
                ProfilingUtils.identifier(SocialNetwork.IG, profile.status()));
        this.telegram = ObjectUtils.firstNonNull(
                ProfilingUtils.identifier(SocialNetwork.TG, profile.site()),
                ProfilingUtils.identifier(SocialNetwork.TG, profile.status()));
        this.isFriend = BoolInt.YES == profile.isFriend();
        this.isPrivate = Boolean.parseBoolean(profile.isPrivate());
        this.isDeactivated = StringUtils.isNotEmpty(profile.deactivated());

        this.university = VKUtils.safeStream(profile.universities())
                .map(University::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.school = VKUtils.safeStream(profile.schools())
                .map(School::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // ocupation

        this.job = VKUtils.safeStream(profile.career())
                .map(Career::getCompany)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean isActive() {
        return !isPrivate && !isDeactivated;
    }

    public String identifier() {
        return username != null ? username : ("id" + id);
    }

    @Override
    public String name() {
        return type().link(
                fullName,
                identifier());
    }

    @Override
    public SocialNetwork type() {
        return SocialNetwork.VK;
    }
}
