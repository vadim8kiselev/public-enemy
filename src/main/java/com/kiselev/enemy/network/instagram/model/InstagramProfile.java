package com.kiselev.enemy.network.instagram.model;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.model.Info;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
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
    private List<ReelMedia> stories;

    @ToString.Exclude
    private List<InstagramProfile> friends;

    @ToString.Exclude
    private List<InstagramProfile> followers;

    @ToString.Exclude
    private List<InstagramProfile> following;

    @ToString.Exclude
    private List<InstagramPost> posts;

    @ToString.Exclude
    private List<InstagramProfile> likes;

//    @ToString.Exclude
//    private List<InstagramPost> stories;

    private LocalDateTime timestamp;

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

        this.public_phone_number = profile.getPhoneNumber();
        this.public_email = profile.getEmail();

//        this.location = location(profile);
    }

    @Override
    public String name() {
        return String.format(
                type().template(),
                username, //StringUtils.isNoneEmpty(fullName) ? fullName : username,
                username
        );
    }

    @Override
    public SocialNetwork type() {
        return SocialNetwork.IG;
    }
}
