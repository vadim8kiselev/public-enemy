package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * InstagramItem
 *
 * @author Ozan Karaali
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramItem extends StatusResult {
    public static final int PHOTO = 1;
    public static final int VIDEO = 2;
    public static final int ALBUM = 8;
    public long taken_at;
    public long pk;
    public String id;
    public long device_timestamp;
    public int media_type;
    public String code;
    public String client_cache_key;
    public int filter_type;
    public boolean has_audio;
    public double video_duration;
    public Map<String, Object> attribution;
    public List<ImageMeta> video_versions;
    public ImageVersions image_versions2;
    public int original_width;
    public int original_height;
    public int number_of_qualities;
    public InstagramUser user;
    public String organic_tracking_token;
    public boolean can_viewer_reshare;
    public boolean caption_is_edited;
    public InstagramComment caption;
    public boolean photo_of_you;
    public boolean comments_disabled;
    public boolean can_viewer_save;
    public boolean has_viewer_saved;
    private String visibility;
    private boolean is_reel_media;
    private boolean can_reply;
}
