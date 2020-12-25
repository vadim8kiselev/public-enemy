package com.kiselev.enemy.network.instagram.api.internal.payload;

import com.kiselev.enemy.network.instagram.api.internal.storymetadata.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramStoryItem extends InstagramItem {
    private String adaction;
    private String link_text;
    private boolean story_is_saved_to_archive;
    private List<InstagramUser> viewers;
    private int viewer_count;
    private int viewer_cursor;
    private int total_viewer_count;
    private List<StoryHashtagItem> story_hashtags;
    private List<StoryPollItem> story_polls;
    private List<ReelMentionItem> reel_mentions;
    private List<StoryLocationItem> story_locations;
    private List<StoryCta> story_cta;
    private List<StoryCountdownItem> story_countdowns;
    private List<StoryQuestionItem> story_questions;
    private List<StoryQuestionResponderInfo> story_question_responder_infos;
    private List<StorySliderItem> story_sliders;
    private List<StorySliderVoterInfo> story_slider_voter_infos;
}
