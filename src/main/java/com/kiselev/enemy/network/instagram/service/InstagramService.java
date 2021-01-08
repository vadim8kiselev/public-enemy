package com.kiselev.enemy.network.instagram.service;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.UserTags;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
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
        List<User> friends = api.friends(Long.valueOf(id));
        return friends.stream()
                .map(InstagramInternalProfile::new)
                .collect(Collectors.toList());
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

    private List<ReelMedia> stories(String id) {
        return api.stories(Long.valueOf(id));
    }


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
        public List<ReelMedia> stories() {
            if (super.stories() == null) {
                super.stories(InstagramService.this.stories(id()));
            }
            return super.stories();
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

        @Override
        public List<InstagramProfile> likes() {
            if (super.likes() == null) {
                List<InstagramPost> posts = posts();

                List<InstagramProfile> likes = posts.stream()
                        .map(InstagramPost::likes)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                super.likes(likes);
            }
            return super.likes();
        }
    }

    private class InstagramInternalPost extends InstagramPost {

        public InstagramInternalPost(TimelineMedia post) {
            super(post);
            propagateTags(post);
        }

        public void propagateTags(TimelineMedia post) {
            if (super.tags() == null) {
                UserTags usertags = post.getUsertags();

                if (usertags != null) {
                    super.tags(usertags.getIn().stream()
                            .map(UserTags.UserTag::getUser)
                            .map(Profile::getUsername)
                            .map(InstagramService.this::profile)
                            .collect(Collectors.toList()));
                }
            }
        }

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
