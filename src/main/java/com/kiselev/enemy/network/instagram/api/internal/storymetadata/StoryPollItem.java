package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StoryPollItem extends StoryItem {
    private PollStickerItem poll_sticker;
    private boolean viewer_can_vote;
    private boolean is_shared_result;
    private boolean finished;

    @Data
    public static class PollStickerItem {
        private String id;
        private long poll_id;
        private String question;
        private List<TallyItem> tallies;

        @Data
        public static class TallyItem {
            private String text;
            private int count;
            private int font_size;
        }
    }
}
