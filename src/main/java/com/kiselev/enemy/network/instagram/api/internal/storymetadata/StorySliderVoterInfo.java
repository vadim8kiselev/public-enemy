package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramUser;
import lombok.Data;

import java.util.List;

@Data
public class StorySliderVoterInfo {
    private long slider_id;
    private List<SliderVoter> voters;
    private String max_id;
    private boolean more_available;
    private long latest_slider_vote_time;

    @Data
    public static class SliderVoter {
        private InstagramUser user;
        private double vote;
        private long ts;
    }
}
