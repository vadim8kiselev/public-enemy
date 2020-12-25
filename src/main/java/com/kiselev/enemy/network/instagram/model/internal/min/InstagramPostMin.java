package com.kiselev.enemy.network.instagram.model.internal.min;

import com.kiselev.enemy.network.instagram.model.InstagramCommentary;
import com.kiselev.enemy.network.instagram.model.InstagramPhoto;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import lombok.Data;
import org.brunocvcunha.instagram4j.requests.payload.FeedItemLocation;

import java.util.List;

@Data
public class InstagramPostMin {

    private Long pk;
    private String id;

    private String timestamp;

    private InstagramPhoto mainPhoto;

    private List<InstagramPhoto> photos;

    private List<InstagramProfile> userTags;

    private FeedItemLocation location;
    private Float lng;
    private Float lat;

    private Integer viewCount;

    private List<InstagramPost> carouselMedia;

    private List<InstagramProfile> likers;
    private Integer likeCount;

    private List<InstagramCommentary> commentaries;
    private Integer commentCount;

    private InstagramCommentary caption;
}
