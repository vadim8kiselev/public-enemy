package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Data class which represents response of {@link com.kiselev.enemy.network.instagram.api.internal.constants.requests.InstagramGetStoryViewersRequest}
 *
 * @author Daniele Pancottini
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramGetStoryViewersResult extends StatusResult {

    private List<InstagramUser> users;
    private String next_max_id;
    private int user_count;
    private int total_viewer_count;
    private int total_screenshot_count;
    private InstagramItem updated_media;

}
