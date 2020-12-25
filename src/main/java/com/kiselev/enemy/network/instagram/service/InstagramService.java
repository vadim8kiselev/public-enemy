package com.kiselev.enemy.network.instagram.service;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.model.InstagramCommentary;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.cache.InstagramCachedAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramService {

    private final InstagramCachedAPI api;

    public InstagramProfile profile(String username) {
        User instagramUser = api.profile(username);
        return new InstagramInternalProfile(instagramUser);
    }

    public List<InstagramProfile> friends(String id) {
        List<InstagramProfile> friends = Lists.newArrayList();

        List<InstagramProfile> followers = followers(id);
        List<InstagramProfile> following = following(id);

        friends.addAll(followers);
        friends.retainAll(following);

        return friends;
    }

    private List<InstagramProfile> followers(String id) {
        List<User> followers = api.followers(Long.valueOf(id));
        return followers.stream()
                .map(InstagramInternalProfile::new)
                .collect(Collectors.toList());
    }

    private List<InstagramProfile> following(String id) {
        List<User> following = api.following(Long.valueOf(id));
        return following.stream()
                .map(InstagramInternalProfile::new)
                .collect(Collectors.toList());
    }

    private List<InstagramPost> posts(String id) {
        List<TimelineMedia> posts = api.posts(Long.valueOf(id));
        return posts.stream()
                .map(InstagramInternalPost::new)
                .collect(Collectors.toList());
    }

    private List<InstagramProfile> likes(String id) {
        List<User> likes = api.likes(id);
        return likes.stream()
                .map(InstagramInternalProfile::new)
                .collect(Collectors.toList());
    }

    private List<InstagramCommentary> commentaries(String id) {
        List<Comment> commentaries = api.commentaries(id);
        return commentaries.stream()
                .map(InstagramCommentary::new)
                .collect(Collectors.toList());
    }

//    private InstagramProfile readProfile(InstagramUser instagramUser) {
//        InstagramProfile profile = mapper.profile(
//                instagramUser
//        );
//
//        /*List<InstagramPhoto> photos = readPhotos(
//                instagramUser
//        );
//        profile.setPhotos(
//                photos
//        );
//        profile.setMainPhoto(
//                InstagramUtils.selectMainPhoto(photos)
//        );*/
//
//        profile.setStories(
//                readStories(instagramUser)
//        );
//
//        return profile;
//    }
//
//
//        post.setLikers(readLikers(instagramFeedItem));
//
//        post.setCommentaries(readCommentaries(instagramFeedItem));
//
//        post.setCaption(mapper.commentary(instagramFeedItem.getCaption()));
//
//        return post;
//    }
//
//    private List<InstagramPhoto> readPhotos(InstagramFeedItem instagramFeedItem) {
//        List<ImageMeta> imageMetas = Lists.newArrayList();
//
//        Optional.ofNullable(instagramFeedItem)
//                .map(InstagramFeedItem::getVideo_versions)
//                .ifPresent(
//                        imageMetas::addAll
//                );
//
//        Optional.ofNullable(instagramFeedItem)
//                .map(InstagramFeedItem::getImage_versions2)
//                .map(ImageVersions::getCandidates)
//                .ifPresent(
//                        imageMetas::addAll
//                );
//
//        return imageMetas.stream()
//                .map(mapper::photo)
//                .sorted(Comparator.comparing(InstagramPhoto::getUrl))
//                .collect(Collectors.toList());
//    }
//
//    private List<InstagramProfile> readLikers(InstagramFeedItem instagramFeedItem) {
//        List<InstagramUserSummary> rawLikers = api.likers(instagramFeedItem.getPk());
//
//        List<InstagramUser> likers = rawLikers.stream()
//                .map(InstagramUserSummary::getUsername)
//                .map(api::profile)
//                .collect(Collectors.toList());
//
//        return readProfiles(
//                likers
//        );
//    }
//
//    private List<InstagramCommentary> readCommentaries(InstagramFeedItem instagramFeedItem) {
//        List<InstagramComment> comments = api.commentaries(instagramFeedItem.getId());
//
//        return comments.stream()
//                .map(mapper::commentary)
//                .collect(Collectors.toList());
//    }
//
//    private List<InstagramProfile> readUserTags(InstagramFeedItem instagramFeedItem) {
//        Stream<InstagramFeedUserTag> userTagStream = Optional.ofNullable(instagramFeedItem)
//                .map(InstagramFeedItem::getUsertags)
//                .map(InstagramUserTagsContainer::getIn)
//                .map(InstagramUtils::safeStream)
//                .orElseGet(Stream::empty);
//
//        List<InstagramUser> userTags = userTagStream
//                .map(InstagramFeedUserTag::getUser)
//                .map(InstagramUserSummary::getUsername)
//                .map(api::profile)
//                .collect(Collectors.toList());
//
//        return readProfiles(
//                userTags
//        );
//    }
//
//    private List<InstagramStory> readStories(InstagramUser instagramUser) {
//        List<InstagramStoryItem> stories = api.stories(instagramUser.getPk());
//
//        return stories.stream()
//                .map(this::readStory)
//                .sorted(Comparator.comparing(InstagramStory::getPk))
//                .collect(Collectors.toList());
//    }
//
//    private InstagramStory readStory(InstagramStoryItem instagramStoryItem) {
//        InstagramStory story = mapper.story(instagramStoryItem);
//
//        List<InstagramPhoto> photos = readPhotos(instagramStoryItem);
//
//        story.setPhotos(
//                photos
//        );
//
//        story.setMainPhoto(
//                InstagramUtils.selectMainPhoto(
//                        photos
//                )
//        );
//
//        story.setViewers(
//                readStoryViewers(instagramStoryItem)
//        );
//
//        return story;
//    }
//
//
//    private List<InstagramProfile> readStoryViewers(InstagramStoryItem instagramStoryItem) {
//        if (api.isThatMe(instagramStoryItem.getUser().getUsername())) {
//            List<InstagramUser> rawStoryViewers = api.storyViewers(
//                    instagramStoryItem.getId()
//            );
//
//            List<String> storyViewers = rawStoryViewers.stream()
//                    .map(InstagramUser::getUsername)
//                    .collect(Collectors.toList());
//
//            return storyViewers.stream()
//                    .map(api::profile)
//                    .map(this::readProfile)
//                    .sorted(Comparator.comparing(InstagramProfile::id))
//                    .collect(Collectors.toList());
//        }
//        return Collections.emptyList();
//    }

    private class InstagramInternalProfile extends InstagramProfile {
        public InstagramInternalProfile(User profile) {
            super(profile);
        }

        @Override
        public List<InstagramProfile> friends() {
            if (super.friends() == null) {
                super.friends(InstagramService.this.friends(id()));
            }
            return super.friends();
        }

        @Override
        public List<InstagramProfile> followers() {
            if (super.followers() == null) {
                super.followers(InstagramService.this.followers(id()));
            }
            return super.followers();
        }

        @Override
        public List<InstagramProfile> following() {
            if (super.following() == null) {
                super.following(InstagramService.this.following(id()));
            }
            return super.following();
        }

        @Override
        public List<InstagramPost> posts() {
            if (super.posts() == null) {
                super.posts(InstagramService.this.posts(id()));
            }
            return super.posts();
        }
    }

    private class InstagramInternalPost extends InstagramPost {

        public InstagramInternalPost(TimelineMedia post) {
            super(post);
        }

        //        @Override
//        public List<InstagramProfile> tags() {
//            if (super.tags() == null) {
//                List<String> tagsIds = super.tags().stream()
//                        .map(InstagramProfile::id)
//                        .collect(Collectors.toList());
//
//                List<InstagramUser> tags = api.profiles(tagsIds);
//
//                super.tags(tags.stream()
//                        .map(InstagramProfile::new)
//                        .collect(Collectors.toList()));
//
//            }
//            return Lists.newArrayList(
//                    super.tags()
//            );
//        }

        @Override
        public List<InstagramProfile> likes() {
            if (super.likes() == null) {
                super.likes(InstagramService.this.likes(id()));
            }
            return super.likes();
        }

        @Override
        public List<InstagramCommentary> commentaries() {
            if (super.commentaries() == null) {
                super.commentaries(InstagramService.this.commentaries(id()));
            }
            return super.commentaries();
        }
    }
}
