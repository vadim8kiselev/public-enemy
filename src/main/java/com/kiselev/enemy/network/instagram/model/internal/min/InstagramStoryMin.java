package com.kiselev.enemy.network.instagram.model.internal.min;

import com.kiselev.enemy.network.instagram.model.InstagramCommentary;
import com.kiselev.enemy.network.instagram.model.InstagramPhoto;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import lombok.Data;
import org.brunocvcunha.instagram4j.requests.payload.StoryCta;
import org.brunocvcunha.instagram4j.storymetadata.*;

import java.util.List;

@Data
public class InstagramStoryMin {

    private Long pk; // ?

    private String id; // ?

    private String timestamp;

    private InstagramCommentary description;

    private List<InstagramProfile> viewers;

    private Integer viewerCount;

    private Integer totalViewerCount; // ?

    private InstagramPhoto photo;

    private List<InstagramPhoto> photos;

    private List<StoryCta> storyCta;
    private List<StoryHashtagItem> storyHashtags;
    private List<StoryPollItem> storyPolls;
    private List<ReelMentionItem> reelMentions;
    private List<StoryLocationItem> storyLocations;
    private List<StoryCountdownItem> storyCountdowns;
    private List<StoryQuestionItem> storyQuestions;
    private List<StoryQuestionResponderInfo> storyQuestionResponderInfos;
    private List<StorySliderItem> storySliders;
    private List<StorySliderVoterInfo> storySliderVoterInfos;
}
