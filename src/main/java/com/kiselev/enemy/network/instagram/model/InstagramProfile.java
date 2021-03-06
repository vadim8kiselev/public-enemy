package com.kiselev.enemy.network.instagram.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.model.Info;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@Document
@NoArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstagramProfile implements Info {

    private String id;

    @Id
    @EqualsAndHashCode.Include
    private String username;

    private String fullName;
    private String category;
    private String biography;

    private String vk;
    private String telegram;

    private String external_url;

    private boolean has_anonymous_profile_picture;

    private boolean is_private;
    private boolean is_verified;
    private boolean is_business;
    private boolean is_favorite;
    private boolean is_deleted;

    private URL photo;

    private String public_phone_number;
    private String public_email;

    private String location;

    @ToString.Exclude
    private List<InstagramStory> stories;

    @ToString.Exclude
    private List<InstagramProfile> friends;

    @ToString.Exclude
    private List<InstagramProfile> unfollowers;

    @ToString.Exclude
    private List<InstagramProfile> followers;

    @ToString.Exclude
    private List<InstagramProfile> following;

    @ToString.Exclude
    private List<InstagramPost> posts;

    @ToString.Exclude
    private List<InstagramProfile> likes;

    private String profileType;

    private boolean isBot;

    private Integer followerCount;

    private Integer followingCount;

    private LocalDateTime timestamp;

    public InstagramProfile(Profile profile) {
        this.timestamp = LocalDateTime.now();

        this.id = String.valueOf(profile.id());
        this.username = profile.username();
        this.fullName = profile.fullName();

        this.has_anonymous_profile_picture = profile.hasAnonymousPhoto();

        this.is_private = profile.isPrivate();
        this.is_deleted = profile.isDeleted();

        this.is_verified = profile.isVerified();
    }

    public InstagramProfile(User profile) {
        this.timestamp = LocalDateTime.now();

        this.id = String.valueOf(profile.id());
        this.username = profile.username();
        this.fullName = profile.fullName();
        this.category = profile.getCategory();
        this.biography = profile.getBiography();

        this.external_url = profile.getExternal_url();
        this.vk = ProfilingUtils.identifier(SocialNetwork.VK, profile.getExternal_url());
        this.telegram = ProfilingUtils.identifier(SocialNetwork.TG, profile.getExternal_url());

        this.has_anonymous_profile_picture = profile.hasAnonymousPhoto();

        this.is_private = profile.isPrivate();
        this.is_deleted = profile.isDeleted();

        this.is_verified = profile.isVerified();
        this.is_business = profile.isBusiness();
        this.is_favorite = profile.isFavorite();

        User.ProfilePic info = profile.getHd_profile_pic_url_info();
        if (info != null) {
            this.photo = info.getUrl();
        }

        this.public_phone_number = ObjectUtils.firstNonNull(
                profile.getContactPhoneNumber(),
                profile.getPublicPhoneNumber()
        );

        this.public_email = profile.getEmail();

        this.location = StringUtils.isNotEmpty(profile.getAddress())
                ? profile.getAddress()
                : null;

        ProfileType type = profileType(profile);
        this.profileType = type.value();

        this.isBot = ProfileType.BOT == type;

        this.followerCount = profile.getFollowerCount();
        this.followingCount = profile.getFollowingCount();
    }

    private ProfileType profileType(User profile) {
        boolean isAnonymous = profile.hasAnonymousPhoto();
        boolean isNotAnonymous = !profile.hasAnonymousPhoto();
        boolean isEmptyBiography = StringUtils.isEmpty(profile.getBiography());
        boolean isNotEmptyBiography = StringUtils.isNotEmpty(profile.getBiography());
        int mediaCount = profile.getMediaCount();
        int followers = profile.getFollowerCount();
        int followings = profile.getFollowingCount();

        Map<ProfileType, Integer> rating = Maps.newHashMap();

        rating.put(ProfileType.BOT, sum(
                rate(isAnonymous, 100)
//                rate(isEmptyBiography),
//                rate(mediaCount < 10),
//                rate(xTimesMore(followings, followers, 5))
        ));
        rating.put(ProfileType.VIEWER, sum(
//                rate(isNotAnonymous),
//                rate(isNotEmptyBiography),
//                rate(mediaCount < 10),
                rate(followings > followers, 100)
        ));
        rating.put(ProfileType.CREATOR, sum(
//                rate(isNotAnonymous),
//                rate(isNotEmptyBiography),
//                rate(mediaCount >= 10),
//                rate(xTimesMore(followers, followings, 5))
                rate(followings < followers, 100)
        ));
        rating.put(ProfileType.BLOGGER, sum(
//                rate(isNotAnonymous),
                rate(mediaCount >= 100)
//                rate(isNotEmptyBiography),
//                rate(xTimesMore(followers, followings, 10))
        ));
        rating.put(ProfileType.STAR, sum(
//                rate(isNotAnonymous),
//                rate(isNotEmptyBiography),
//                rate(mediaCount >= 1000),
                rate(followers >= 1_000_000, 100)
        ));

        return rating.entrySet().stream()
                .sorted(Map.Entry.<ProfileType, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("A problem with the rating mechanism"));
    }

    private Integer sum(int... rates) {
        return IntStream.of(rates)
                .sum();
    }

    private boolean xTimesMore(int a, int b, int x) {
        if (b == 0) {
            return false;
        }
        return a / b > x;
    }

    private boolean xTimesLess(int a, int b, int x) {
        if (b == 0) {
            return false;
        }
        return a / b < x;
    }

    private int rate(boolean expression) {
        return expression ? 1 : 0;
    }

    private int rate(boolean expression, int rate) {
        return expression ? rate : 0;
    }

    public List<InstagramProfile> followers() {
        if (followers != null) {
            return Lists.newArrayList(followers);
        }
        return null;
    }

    public List<InstagramProfile> following() {
        if (following != null) {
            return Lists.newArrayList(following);
        }
        return null;
    }

    public String identifier() {
        return username;
    }

    @Override
    public String name() {
        return String.format(
                type().template(),
                username,
                username
        );
    }

    @Override
    public SocialNetwork type() {
        return SocialNetwork.IG;
    }
}