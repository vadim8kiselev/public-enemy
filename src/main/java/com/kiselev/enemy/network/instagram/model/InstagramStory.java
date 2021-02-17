package com.kiselev.enemy.network.instagram.model;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersionsMeta;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.VideoVersionsMeta;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelImageMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelVideoMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineImageMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineVideoMedia;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import com.kiselev.enemy.utils.image.Media;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstagramStory {

    @EqualsAndHashCode.Include
    private String id;

    private String postId;

    private LocalDateTime date;

    private MediaType type;

    private Media photo;

    private Media video;

    @ToString.Exclude
    private List<InstagramProfile> viewers;

    private String caption;

    public InstagramStory(ReelMedia story) {

        this.id = story.getPk();

        this.postId = story.getId();

        this.date = InstagramUtils.dateAndTime(story.getTaken_at());

        this.type = MediaType.by(story.getMedia_type());

        Comment.Caption caption = story.getCaption();
        if (caption != null) {
            this.caption = caption.getText();
        }

        switch (type) {
            case PHOTO: {
                this.photo = photo(story);
                break;
            }
            case VIDEO: {
                this.video = video(story);
                break;
            }
            default:
                throw new RuntimeException("Unknown type of story media");
        }
    }

    private Media photo(ReelMedia reelMedia) {
        ReelImageMedia story = (ReelImageMedia) reelMedia;
        ImageVersions image_versions2 = story.getImage_versions2();
        if (image_versions2 != null) {
            return image_versions2.getCandidates().stream()
                    .max(Comparator.comparingInt(imageMeta -> imageMeta.getHeight() * imageMeta.getWidth()))
                    .map(ImageVersionsMeta::getUrl)
                    .map(Media::of)
                    .orElse(null);
        }
        return null;
    }

    private Media video(ReelMedia reelMedia) {
        ReelVideoMedia story = (ReelVideoMedia) reelMedia;
        List<VideoVersionsMeta> videoVersions = story.getVideo_versions();
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
