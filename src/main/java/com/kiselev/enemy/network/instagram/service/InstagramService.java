package com.kiselev.enemy.network.instagram.service;

import com.kiselev.enemy.network.instagram.api.InstagramAPI;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.UserTags;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.model.InstagramCommentary;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.model.InstagramStory;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramService extends ProgressableAPI {

    private final InstagramAPI api;

    @Value("${com.kiselev.enemy.instagram.identifier:}")
    private String igIdentifier;

    public InstagramAPI api() {
        return api;
    }

    public InstagramProfile me() {
        return profile(igIdentifier);
    }

    public InstagramProfile profile(String username) {
        if (username == null) {
            return null;
        }

        User instagramUser = api.profile(username);
        return new InstagramInternalProfile(instagramUser);
    }

    public String location(InstagramProfile profile) {
        List<InstagramPost> posts = profile.posts();
        Prediction<String> prediction = AnalyticsUtils.predict(InstagramPost::location, posts);
        if (prediction != null && prediction.sufficient(20)) {
            return prediction.value();
        }
        return null;
    }

    public List<InstagramProfile> friends(String id) {
        List<Profile> friends = api.friends(id);
        return convert(friends);
    }

    public List<InstagramProfile> unfollowers(String id) {
        List<Profile> unfollowers = api.unfollowers(id);
        return convert(unfollowers);
    }

    public List<InstagramProfile> unfollowings(String id) {
        List<Profile> unfollowings = api.unfollowings(id);
        return convert(unfollowings);
    }

    public List<InstagramProfile> followers(String id) {
        List<Profile> followers = api.followers(id);
        return convert(followers);
    }

    public List<InstagramProfile> following(String id) {
        List<Profile> following = api.following(id);
        return convert(following);
    }

    public List<InstagramPost> posts(String id) {
        List<TimelineMedia> posts = api.posts(id);
        return posts.stream()
                .map(InstagramInternalPost::new)
                .collect(Collectors.toList());
    }

    public List<InstagramProfile> likes(String id) {
        List<Profile> likes = api.likes(id);
        return convert(likes);
    }

    public List<InstagramCommentary> commentaries(String id) {
        List<Comment> commentaries = api.commentaries(id);
        return commentaries.stream()
                .map(InstagramCommentary::new)
                .collect(Collectors.toList());
    }

    public List<InstagramStory> stories(String id) {
        List<ReelMedia> stories = api.stories(Long.valueOf(id));
        return stories.stream()
                .map(InstagramInternalStory::new)
                .collect(Collectors.toList());
    }

    public List<InstagramProfile> viewers(String id) {
        List<Profile> viewers = api.viewers(id);
        return convert(viewers);
    }

    public Map<Profile, Set<ThreadItem>> history() {
        return api.history();
//        Map<Profile, Set<ThreadItem>> history = api.history();
//        return history.entrySet().stream()
//                .collect(Collectors.toMap(
//                        entry -> profile(entry.getKey().username()),
//                        Map.Entry::getValue,
//                        (first, second) -> {
//                            if (first.size() != second.size()) {
//                                throw new RuntimeException("An error happened while merging");
//                            }
//                            return second;
//                        }
//                ));
    }

    public Set<ThreadItem> messages(String threadId) {
        return api.messages(threadId);
    }

//    @SneakyThrows
//    public EnemyMessage<InstagramProfile> info(String identifier) {
//        List<String> messages = Lists.newArrayList();
//
//        InstagramProfile profile = profile(identifier);
//        messages.add(message("Username", profile.username()));
//        messages.add(message("Full name", profile.fullName()));
//        messages.add(message("Category", profile.category()));
//        messages.add(message("Biography", profile.biography()));
//        messages.add(message("Phone number", profile.public_phone_number()));
//        messages.add(message("Email", profile.public_email()));
//        messages.add(message("Address", profile.address()));
//
//        String message = messages.stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.joining("\n"));
//        if (StringUtils.isNotEmpty(message)) {
//            return EnemyMessage.of(profile, message);
//        } else {
//            return null;
//        }
//    }

//    private String message(String title, Object field) {
//        if (field != null) {
//            String string = field.toString();
//            if (StringUtils.isNotEmpty(string)) {
//                return title + ": " + field.toString();
//            }
//        }
//        return null;
//    }

//
//
//    public List<InstagramProfile> readStoryViewers(InstagramStoryItem instagramStoryItem) {
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

    private List<InstagramProfile> convert(List<Profile> profiles) {
        return profiles.stream()
                .map(Profile::username)
                .map(this::profile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private class InstagramInternalProfile extends InstagramProfile {

        public InstagramInternalProfile(User profile) {
            super(profile);
        }

        @Override
        public String location() {
            if (super.location() == null) {
                super.location(InstagramService.this.location(this));
            }
            return super.location();
        }

        @Override
        public List<InstagramStory> stories() {
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
        public List<InstagramProfile> unfollowers() {
            if (super.unfollowers() == null) {
                super.unfollowers(InstagramService.this.unfollowers(id()));
            }
            return super.unfollowers();
        }

        @Override
        public List<InstagramProfile> unfollowings() {
            if (super.unfollowings() == null) {
                super.unfollowings(InstagramService.this.unfollowings(id()));
            }
            return super.unfollowings();
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
                            .map(Profile::username)
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

    private class InstagramInternalStory extends InstagramStory {

        public InstagramInternalStory(ReelMedia story) {
            super(story);
        }

        @Override
        public List<InstagramProfile> viewers() {
            if (super.viewers() == null) {
                super.viewers(InstagramService.this.viewers(id()));
            }
            return super.viewers();
        }
    }
}
