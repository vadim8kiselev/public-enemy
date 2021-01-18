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
import com.vk.api.sdk.objects.base.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static com.kiselev.enemy.network.vk.model.constants.VKMessages.*;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VKProfile implements Info {

    @EqualsAndHashCode.Include
    private String id;

    @EnemyValue(message = SCREEN_NAME_MESSAGE)
    private String screenName;

    @EnemyValue(message = FIRST_NAME_MESSAGE)
    private String firstName;

    @EnemyValue(message = LAST_NAME_MESSAGE)
    private String lastName;

    @EnemyValue
    private String fullName;

    @EnemyValue
    private String status;

    @EnemyValue(message = SEX_MESSAGE)
    private Sex sex;

    @EnemyValue(message = AGE_MESSAGE)
    private Integer age;

    @EnemyValue(message = BIRTHDAY_MESSAGE)
    private String birthday;

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
    private String instagram;

    @EnemyValue
    private String telegram;

    private boolean isFriend;

    private String isPrivate;

    @EnemyValue(message = DEACTIVATED_MESSAGE)
    private String isDeactivated;

    @EnemyValues(newMessage = NEW_PHOTO_MESSAGE, deleteMessage = DELETED_PHOTO_MESSAGE)
    private List<Photo> photos;

    @EnemyValues(newMessage = NEW_FRIEND_MESSAGE, deleteMessage = DELETED_FRIEND_MESSAGE)
    private List<VKProfile> friends;

    @EnemyValues(newMessage = NEW_FOLLOWER_MESSAGE, deleteMessage = DELETED_FOLLOWER_MESSAGE)
    private List<VKProfile> followers;

    @EnemyValues(newMessage = NEW_FOLLOWING_MESSAGE, deleteMessage = DELETED_FOLLOWING_MESSAGE)
    private List<VKProfile> following;

    @EnemyValues(newMessage = NEW_GROUP_MESSAGE, deleteMessage = DELETED_GROUP_MESSAGE)
    private List<Group> communities;

    @EnemyValues(newMessage = NEW_POST_MESSAGE, deleteMessage = DELETED_POST_MESSAGE)
    private List<Post> posts;

    private List<VKProfile> relatives;

    private List<VKProfile> likes;

    private LocalDateTime timestamp;

    public VKProfile(Profile profile) {
        this.timestamp = LocalDateTime.now();

        this.id = profile.id();
        this.screenName = profile.screenName();
        this.firstName = profile.firstName();
        this.lastName = profile.lastName();
        this.fullName = profile.firstName() + " " + profile.lastName();
        this.status = profile.status();
        this.sex = profile.sex();
        this.age = VKUtils.age(profile.birthDate());
        this.birthday = profile.birthDate();
        this.photo = profile.photo();
        this.country = VKUtils.title(profile.country());
        this.city = VKUtils.title(profile.city());
        this.countryCode = VKUtils.code(profile.country());
        this.cityCode = VKUtils.code(profile.city());
        this.homeTown = profile.homeTown();
        this.phone = ObjectUtils.firstNonNull(profile.mobilePhone(), profile.mobilePhone());
        this.site = profile.site();
        this.instagram = ObjectUtils.firstNonNull(
                profile.instagram(),
                ProfilingUtils.identifier(SocialNetwork.IG, profile.site()),
                ProfilingUtils.identifier(SocialNetwork.IG, profile.status()));
        this.telegram = ObjectUtils.firstNonNull(
                ProfilingUtils.identifier(SocialNetwork.TG, profile.site()),
                ProfilingUtils.identifier(SocialNetwork.TG, profile.status()));
        this.isFriend = BoolInt.YES == profile.isFriend();
        this.isPrivate = profile.isPrivate();
        this.isDeactivated = profile.deactivated();

//        if (force) {
//            vkProfile.photos();
//            vkProfile.friends();
//            vkProfile.followers();
//            vkProfile.following();
//            vkProfile.communities();
//            vkProfile.posts();
//            vkProfile.relatives();
//        }
    }

    public boolean isActive() {
        return StringUtils.isEmpty(isDeactivated)
                && (isFriend || !Boolean.parseBoolean(isPrivate));
    }

    @Override
    public String name() {
        return String.format(
                type().template(),
                fullName,
                screenName != null ? screenName : ("id" + id)
        );
    }

    @Override
    public SocialNetwork type() {
        return SocialNetwork.VK;
    }
}
