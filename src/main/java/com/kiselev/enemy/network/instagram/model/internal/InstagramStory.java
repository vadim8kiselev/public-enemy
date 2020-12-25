package com.kiselev.enemy.network.instagram.model.internal;

import lombok.Data;
import org.brunocvcunha.instagram4j.requests.payload.StoryCta;
import org.brunocvcunha.instagram4j.storymetadata.*;

import java.util.List;
import java.util.Map;

@Data
public class InstagramStory {

    // Min
    private Long pk;
    private String id;

    private String takenAt;
    private Long deviceTimestamp;
    
    private Integer mediaType;

    private InstagramCommentary caption;

    private List<InstagramProfile> viewers;
    private Integer viewerCount;
    private Integer viewerCursor;
    private Integer totalViewerCount;

    private List<StoryHashtagItem> storyHashtags;
    private List<StoryPollItem> storyPolls;
    private List<ReelMentionItem> reelMentions;
    private List<StoryLocationItem> storyLocations;
    private List<StoryCta> storyCta;
    private List<StoryCountdownItem> storyCountdowns;
    private List<StoryQuestionItem> storyQuestions;
    private List<StoryQuestionResponderInfo> storyQuestionResponderInfos;
    private List<StorySliderItem> storySliders;
    private List<StorySliderVoterInfo> storySliderVoterInfos;

    private InstagramPhoto mainPhoto;

    // Full
    private Integer filterType;
    private Boolean hasAudio;
    
    private Double videoDuration;
    
    private Map<String, Object> attribution;
    
    private List<InstagramPhoto> photos;
    
    private Integer originalWidth;
    private Integer originalHeight;
    private Integer numberOfQualities;

    private String visibility;
    private String adaction;
    private String linkText;
    
    private Boolean canViewerReshare;
    private Boolean captionIsEdited;
    private Boolean photoOfYou;
    private Boolean commentsDisabled;
    private Boolean canViewerSave;
    private Boolean hasViewerSaved;
    private Boolean isReelMedia;
    private Boolean canReply;
    private Boolean storyIsSavedToArchive;

    private String code;
    private String clientCacheKey;
    private String organicTrackingToken;
}
