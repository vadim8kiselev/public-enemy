package com.kiselev.enemy.network.instagram.model.internal.mapper;

import com.kiselev.enemy.network.instagram.utils.ReaderMode;
import com.kiselev.enemy.network.instagram.model.*;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import lombok.RequiredArgsConstructor;
import org.brunocvcunha.instagram4j.requests.payload.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstagramMapper {

    @Value("#{'${com.kiselev.enemy.instagram.reader.mode}'}")
    private ReaderMode mode;

    public InstagramProfile profile(InstagramUser instagramUser) {
        InstagramProfile profile = new InstagramProfile();

        profile.setTimestamp(LocalDateTime.now());

        profile.setId(String.valueOf(instagramUser.getPk()));
        profile.setUsername(instagramUser.getUsername());
        profile.setFullName(instagramUser.getFull_name());

        profile.setMediaCount(instagramUser.getMedia_count());
        profile.setFollowerCount(instagramUser.getFollower_count());
        profile.setFollowingCount(instagramUser.getFollowing_count());
        profile.setGeoMediaCount(instagramUser.getGeo_media_count());
        profile.setUserTagsCount(instagramUser.getUsertags_count());

        profile.setBiography(instagramUser.getBiography());
        profile.setCategory(instagramUser.getCategory());
        profile.setLatitude(instagramUser.getLatitude());
        profile.setLongitude(instagramUser.getLongitude());

        profile.setExternalUrl(instagramUser.getExternal_url());

        profile.setIsPrivate(instagramUser.is_private());
        profile.setIsVerified(instagramUser.is_verified());
        profile.setIsBusiness(instagramUser.is_business());

        if (ReaderMode.FULL == mode) {
            profile.setPublicEmail(instagramUser.getPublic_email());
            profile.setPublicPhoneNumber(instagramUser.getPublic_phone_number());
            profile.setPublicPhoneCountryCode(instagramUser.getPublic_phone_country_code());

            profile.setAddressStreet(instagramUser.getAddress_street());
            profile.setCityName(instagramUser.getCity_name());
            profile.setZip(instagramUser.getZip());

            profile.setDirectMessaging(instagramUser.getDirect_messaging());
            profile.setBusinessContactMethod(instagramUser.getBusiness_contact_method());

            profile.setProfilePicId(instagramUser.getProfile_pic_id());
            profile.setProfilePicUrl(instagramUser.getProfile_pic_url());

            profile.setHasChaining(instagramUser.isHas_chaining());
            profile.setIsFavorite(instagramUser.is_favorite());

            profile.setHasBiographyTranslation(instagramUser.isHas_biography_translation());
            profile.setHasAnonymousProfilePicture(instagramUser.isHas_anonymous_profile_picture());

            profile.setExternalLynxUrl(instagramUser.getExternal_lynx_url());
        }

        return profile;
    }

    public InstagramPost post(InstagramFeedItem instagramFeedItem) {
        InstagramPost post = new InstagramPost();

        post.setId(instagramFeedItem.getId());
        post.setPk(instagramFeedItem.getPk());
        post.setTakenAt(
                InstagramUtils.dateAndTime(
                        instagramFeedItem.getTaken_at()
                )
        );
        post.setDeviceTimestamp(instagramFeedItem.getDevice_timestamp());
        post.setMediaType(instagramFeedItem.getMedia_type());

        post.setViewCount(instagramFeedItem.getView_count());
        post.setLikeCount(instagramFeedItem.getLike_count());
        post.setCommentCount(instagramFeedItem.getComment_count());

        post.setLocation(instagramFeedItem.getLocation());
        post.setLng(instagramFeedItem.getLng());
        post.setLat(instagramFeedItem.getLat());

        if (ReaderMode.FULL == mode) {
            post.setFilterType(instagramFeedItem.getFilter_type());
            post.setHasAudio(instagramFeedItem.isHas_audio());
            post.setVideoDuration(instagramFeedItem.getVideo_duration());
            post.setAttribution(instagramFeedItem.getAttribution());

            post.setOriginalWidth(instagramFeedItem.getOriginal_width());
            post.setOriginalHeight(instagramFeedItem.getOriginal_height());

            post.setTopLikers(instagramFeedItem.getTop_likers());
            post.setNextMaxId(instagramFeedItem.getNext_max_id());
            post.setMaxNumVisiblePreviewComments(instagramFeedItem.getMax_num_visible_preview_comments());
            post.setHasLiked(instagramFeedItem.isHas_liked());
            post.setCommentLikesEnabled(instagramFeedItem.isComment_likes_enabled());
            post.setHasMoreComments(instagramFeedItem.isHas_more_comments());
            post.setCanViewerReshare(instagramFeedItem.isCan_viewer_reshare());
            post.setCaptionIsEdited(instagramFeedItem.isCaption_is_edited());
            post.setPhotoOfYou(instagramFeedItem.isPhoto_of_you());
            post.setCommentsDisabled(instagramFeedItem.isComments_disabled());
            post.setCanViewerSave(instagramFeedItem.isCan_viewer_save());
            post.setHasViewerSaved(instagramFeedItem.isHas_viewer_saved());

            post.setCode(instagramFeedItem.getCode());
            post.setClientCacheKey(instagramFeedItem.getClient_cache_key());
            post.setOrganicTrackingToken(instagramFeedItem.getOrganic_tracking_token());
        }

        return post;
    }

    public InstagramCommentary commentary(InstagramComment instagramComment) {
        if (instagramComment == null) {
            return null;
        }

        InstagramCommentary instagramCommentary = new InstagramCommentary();

        instagramCommentary.setPk(instagramComment.getPk());
        instagramCommentary.setUserId(instagramComment.getUser_id());
        instagramCommentary.setMediaId(instagramComment.getMedia_id());
        instagramCommentary.setType(instagramComment.getType());
        instagramCommentary.setText(instagramComment.getText());
        instagramCommentary.setCreatedAt(
                InstagramUtils.dateAndTime(
                        instagramComment.getCreated_at()
                )
        );
        instagramCommentary.setCreatedAtUTC(
                InstagramUtils.dateAndTime(
                        instagramComment.getCreated_at_utc()
                )
        );
        instagramCommentary.setCommentLikeCount(instagramComment.getComment_like_count());

        if (ReaderMode.FULL == mode) {
            instagramCommentary.setContentType(instagramComment.getContent_type());
            instagramCommentary.setStatus(instagramComment.getStatus());
            instagramCommentary.setBitFlags(instagramComment.getBit_flags());
            instagramCommentary.setDidReportAsSpam(instagramComment.isDid_report_as_spam());
            instagramCommentary.setShareEnabled(instagramComment.isShare_enabled());
        }

        return instagramCommentary;
    }

    private List<InstagramPhoto> photos(List<ImageMeta> imageMetas) {
        return InstagramUtils.safeStream(imageMetas)
                .map(this::photo)
                .sorted(Comparator.comparing(InstagramPhoto::getUrl))
                .collect(Collectors.toList());
    }

    public InstagramPhoto photo(ImageMeta imageMeta) {
        InstagramPhoto photo = new InstagramPhoto();

        photo.setUrl(imageMeta.getUrl());
        photo.setHeight(imageMeta.getHeight());
        photo.setWidth(imageMeta.getWidth());

        return photo;
    }

    public InstagramPhoto photo(InstagramProfilePic instagramProfilePic) {
        InstagramPhoto photo = new InstagramPhoto();

        photo.setUrl(instagramProfilePic.getUrl());
        photo.setHeight(instagramProfilePic.getHeight());
        photo.setWidth(instagramProfilePic.getWidth());

        return photo;
    }

    public InstagramStory story(InstagramStoryItem instagramStoryItem) {
        InstagramStory instagramStory = new InstagramStory();

        instagramStory.setPk(instagramStoryItem.getPk());
        instagramStory.setId(instagramStoryItem.getId());

        instagramStory.setTakenAt(
                InstagramUtils.dateAndTime(
                        instagramStoryItem.getTaken_at()
                )
        );
        instagramStory.setDeviceTimestamp(instagramStoryItem.getDevice_timestamp());

        instagramStory.setMediaType(instagramStoryItem.getMedia_type());

        /*instagramStory.setViewers(
                InstagramUtils.safeStream(instagramStoryItem.getViewers())
                        .map(this::profile)
                        .collect(Collectors.toList())

        );*/
        instagramStory.setViewerCount(instagramStoryItem.getViewer_count());
        instagramStory.setViewerCursor(instagramStoryItem.getViewer_cursor());
        instagramStory.setTotalViewerCount(instagramStoryItem.getTotal_viewer_count());

        instagramStory.setCaption(
                commentary(instagramStoryItem.getCaption())
        );

        if (ReaderMode.FULL == mode) {
            instagramStory.setFilterType(instagramStoryItem.getFilter_type());
            instagramStory.setHasAudio(instagramStoryItem.isHas_audio());

            instagramStory.setVideoDuration(instagramStoryItem.getVideo_duration());

            instagramStory.setAttribution(instagramStoryItem.getAttribution());

            instagramStory.setOriginalWidth(instagramStoryItem.getOriginal_width());
            instagramStory.setOriginalHeight(instagramStoryItem.getOriginal_height());
            instagramStory.setNumberOfQualities(instagramStoryItem.getNumber_of_qualities());

            instagramStory.setVisibility(instagramStoryItem.getVisibility());
            instagramStory.setAdaction(instagramStoryItem.getAdaction());
            instagramStory.setLinkText(instagramStoryItem.getLink_text());

            instagramStory.setCanViewerReshare(instagramStoryItem.isCan_viewer_reshare());
            instagramStory.setCaptionIsEdited(instagramStoryItem.isCaption_is_edited());
            instagramStory.setPhotoOfYou(instagramStoryItem.isPhoto_of_you());
            instagramStory.setCommentsDisabled(instagramStoryItem.isComments_disabled());
            instagramStory.setCanViewerSave(instagramStoryItem.isCan_viewer_save());
            instagramStory.setHasViewerSaved(instagramStoryItem.isHas_viewer_saved());
            instagramStory.setIsReelMedia(instagramStoryItem.is_reel_media());
            instagramStory.setCanReply(instagramStoryItem.isCan_reply());
            instagramStory.setStoryIsSavedToArchive(instagramStoryItem.isStory_is_saved_to_archive());

            instagramStory.setStoryHashtags(instagramStoryItem.getStory_hashtags());
            instagramStory.setStoryPolls(instagramStoryItem.getStory_polls());
            instagramStory.setReelMentions(instagramStoryItem.getReel_mentions());
            instagramStory.setStoryLocations(instagramStoryItem.getStory_locations());
            instagramStory.setStoryCta(instagramStoryItem.getStory_cta());
            instagramStory.setStoryCountdowns(instagramStoryItem.getStory_countdowns());
            instagramStory.setStoryQuestions(instagramStoryItem.getStory_questions());
            instagramStory.setStoryQuestionResponderInfos(instagramStoryItem.getStory_question_responder_infos());
            instagramStory.setStorySliders(instagramStoryItem.getStory_sliders());
            instagramStory.setStorySliderVoterInfos(instagramStoryItem.getStory_slider_voter_infos());

            instagramStory.setCode(instagramStoryItem.getCode());
            instagramStory.setClientCacheKey(instagramStoryItem.getClient_cache_key());
            instagramStory.setOrganicTrackingToken(instagramStoryItem.getOrganic_tracking_token());
        }

        return instagramStory;
    }
}
