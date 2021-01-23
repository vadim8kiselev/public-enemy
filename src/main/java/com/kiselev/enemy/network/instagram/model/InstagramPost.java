package com.kiselev.enemy.network.instagram.model;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.location.Location;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersionsMeta;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.VideoVersionsMeta;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.*;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import com.kiselev.enemy.utils.image.Media;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstagramPost {

    @EqualsAndHashCode.Include
    private String id;

    private String postId;

    private String date;

    private MediaType type;

    private String location;

    private Media photo;

    private Media video;

    private List<Media> album;

    @ToString.Exclude
    private List<InstagramProfile> tags;

    private String caption;

    @ToString.Exclude
    private List<InstagramProfile> likes;

    @ToString.Exclude
    private List<InstagramCommentary> commentaries;

    private Integer likeCount;

    private Integer commentariesCount;

    public InstagramPost(TimelineMedia post) {
        this.id = post.getPk();

        this.postId = post.getId();

        this.date = InstagramUtils.dateAndTime(post.getTaken_at());

        this.type = MediaType.by(post.getMedia_type());

        this.location = location(post.getLocation());

        Comment.Caption caption = post.getCaption();
        if (caption != null) {
            this.caption = caption.getText();
        }

        switch (type) {
            case PHOTO: {
                this.photo = photo(post);
                break;
            }
            case VIDEO: {
                this.video = video(post);
                break;
            }
            case ALBUM: {
                this.album = album(post);
                break;
            }
        }

        this.likeCount = post.getLike_count();

        this.commentariesCount = post.getComment_count();
    }

    private String location(Location location) {
        if (location != null) {
            return Stream.of(location.getCity(),
                    location.getAddress(),
                    location.getShort_name(),
                    location.getName())
                    .filter(StringUtils::isNotEmpty)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private Media photo(TimelineMedia timelineMedia) {
        TimelineImageMedia post = (TimelineImageMedia) timelineMedia;
        com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions image_versions2 = post.getImage_versions2();
        if (image_versions2 != null) {
            return image_versions2.getCandidates().stream()
                    .max(Comparator.comparingInt(imageMeta -> imageMeta.getHeight() * imageMeta.getWidth()))
                    .map(ImageVersionsMeta::getUrl)
                    .map(Media::of)
                    .orElse(null);
        }
        return null;
    }

    private Media video(TimelineMedia timelineMedia) {
        TimelineVideoMedia post = (TimelineVideoMedia) timelineMedia;
        List<VideoVersionsMeta> videoVersions = post.getVideo_versions();
        if (videoVersions != null) {
            return videoVersions.stream()
                    .max(Comparator.comparingInt(imageMeta -> imageMeta.getHeight() * imageMeta.getWidth()))
                    .map(VideoVersionsMeta::getUrl)
                    .map(Media::of)
                    .orElse(null);
        }
        return null;
    }

    private List<Media> album(TimelineMedia timelineMedia) {
        TimelineCarouselMedia post = (TimelineCarouselMedia) timelineMedia;
        List<CaraouselItem> carousel_media = post.getCarousel_media();
        if (carousel_media != null) {
            List<Media> albumPhotos = Lists.newArrayList();

            for (CaraouselItem carouselMediaItem : carousel_media) {
                MediaType type = MediaType.by(carouselMediaItem.getMedia_type());

                switch (type) {
                    case PHOTO: {
                        albumPhotos.add(photo(carouselMediaItem));
                        break;
                    }
                    case VIDEO: {
                        albumPhotos.add(video(carouselMediaItem));
                        break;
                    }
                }
            }
            return albumPhotos;
        }
        return null;
    }

    private Media photo(CaraouselItem carouselMediaItem) {
        ImageCaraouselItem post = (ImageCaraouselItem) carouselMediaItem;
        com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions image_versions2 = post.getImage_versions2();
        if (image_versions2 != null) {
            return image_versions2.getCandidates().stream()
                    .max(Comparator.comparingInt(imageMeta -> imageMeta.getHeight() * imageMeta.getWidth()))
                    .map(ImageVersionsMeta::getUrl)
                    .map(Media::of)
                    .orElse(null);
        }
        return null;
    }

    private Media video(CaraouselItem carouselMediaItem) {
        VideoCaraouselItem post = (VideoCaraouselItem) carouselMediaItem;
        List<VideoVersionsMeta> videoVersions = post.getVideo_versions();
        if (videoVersions != null) {
            return videoVersions.stream()
                    .max(Comparator.comparingInt(imageMeta -> imageMeta.getHeight() * imageMeta.getWidth()))
                    .map(VideoVersionsMeta::getUrl)
                    .map(Media::of)
                    .orElse(null);
        }
        return null;
    }
}
