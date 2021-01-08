package com.kiselev.enemy.network.instagram.api;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.client.InstagramClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.feed.Reel;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedUserReelMediaRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedUserRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsFeedsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetCommentsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetLikersRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersUsernameInfoRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserReelsMediaResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUsersResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaGetCommentsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramAPI {

    private final InstagramClient client;

    public List<User> profiles(List<String> usernames) {
        return usernames.stream()
                .map(this::profile)
                .collect(Collectors.toList());
    }

    public User profile(String username) {
//        InstagramSearchUsernameResult userResult =
//                client.request(new InstagramSearchUsernameRequest(username));

        UserResponse response = client.request(
                new UsersUsernameInfoRequest(username)
        );

        if (response != null) {
            User user = response.getUser();
            if (user != null) {
                return user;
            } else {
                User deletedUser = new User();
                deletedUser.setPk(0L);
                deletedUser.setUsername(username);
                return deletedUser;
            }
        }
        return null;
    }

    public List<Profile> friends(Long profilePk) {
        List<Profile> friends = Lists.newArrayList();

        List<Profile> followers = followers(profilePk);
        List<Profile> following = following(profilePk);

        friends.addAll(followers);
        friends.retainAll(following);

        return friends;
    }

    public List<Profile> followers(Long profilePk) {
        List<Profile> followers = Lists.newArrayList();

        String next = null;
        do {
//            InstagramGetUserFollowersResult followersResult =
//                    client.request(new InstagramGetUserFollowersRequest(
//                            profilePk,
//                            next
//                    ));

            FeedUsersResponse response = client.request(
                    new FriendshipsFeedsRequest(profilePk,
                            FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS,
                            next));

            if (response != null) {
                followers.addAll(
                        response.getUsers()
                );
            }

            next = response != null
                    ? response.getNext_max_id()
                    : null;
        } while (next != null);

        return followers;
    }

    public List<Profile> following(Long profilePk) {
        List<Profile> following = Lists.newArrayList();

        String next = null;
        do {
//            InstagramGetUserFollowersResult profileFollowing =
//                    client.request(new InstagramGetUserFollowingRequest(
//                            profilePk,
//                            next
//                    ));

            FeedUsersResponse response = client.request(
                    new FriendshipsFeedsRequest(profilePk,
                            FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING,
                            next));

            if (response != null) {
                following.addAll(
                        response.getUsers()
                );
            }

            next = response != null
                    ? response.getNext_max_id()
                    : null;
        } while (next != null);

        return following;
    }

    public List<TimelineMedia> posts(Long profilePk) {
        List<TimelineMedia> posts = Lists.newArrayList();

        String next = null;
        do {

            FeedUserResponse response = client.request(
                    new FeedUserRequest(profilePk, next)
            );

//            InstagramFeedResult feedPosts =
//                    client.request(new InstagramUserFeedRequest2(
//                            profilePk,
//                            next
//                    ));

            if (response != null) {
                posts.addAll(
                        response.getItems()
                );
            }

            next = response != null
                    ? response.getNext_max_id()
                    : null;
        } while (next != null);

        return posts;
    }

    public List<Profile> likes(String mediaId) {

        FeedUsersResponse response = client.request(
                new MediaGetLikersRequest(mediaId)
        );

//        InstagramGetMediaLikersResult likesResult = client.request(
//                new InstagramGetMediaLikersRequest(
//                        mediaId
//                ));

        if (response != null) {
            return response.getUsers();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Comment> commentaries(String mediaId) {
        List<Comment> comments = Lists.newArrayList();

        String next = null;
        do {

            MediaGetCommentsResponse response = client.request(
                    new MediaGetCommentsRequest(mediaId,
                            next)
            );

//            InstagramGetMediaCommentsResult commentsResult = client.request(
//                    new InstagramGetMediaCommentsRequest(
//                            mediaId,
//                            next
//                    ));

            if (response != null) {
                comments.addAll(
                        response.getComments()
                );
            }

            next = response != null
                    ? response.getNext_max_id()
                    : null;
        } while (next != null);

        return comments;
    }

    public List<ReelMedia> stories(Long profilePk) {
        FeedUserReelsMediaResponse response = client.request(
                new FeedUserReelMediaRequest(profilePk)
        );

        Reel reel = response.getReel();
        if (reel != null) {
            return reel.getItems();
        }
        return null;
    }






//
//    // Also check for InstagramGetReelsTrayFeedRequest
//    public List<InstagramStoryItem> stories(Long profilePk) {
//        InstagramUserReelMediaFeedResult feedResult = client.request(
//                new InstagramGetUserReelMediaFeedRequest(
//                        profilePk
//                )
//        );
//        if (feedResult != null) {
//            List<InstagramStoryItem> items = feedResult.getItems();
//            if (items != null) {
//                return items;
//            }
//        }
//        return Collections.emptyList();
//    }
//
//    public List<InstagramUser> storyViewers(String storyPk) {
//        List<InstagramUser> viewers = Lists.newArrayList();
//
//        String next = null;
//        do {
//            InstagramGetStoryViewersResult viewersResult = client.request(
//                    new InstagramGetStoryViewersRequest2(
//                            storyPk,
//                            next
//                    )
//            );
//
//            if (viewersResult != null) {
//                viewers.addAll(
//                        Optional.of(viewersResult)
//                                .map(InstagramGetStoryViewersResult::getUsers)
//                                .orElseGet(Lists::newArrayList)
//                );
//            }
//
//            next = viewersResult != null
//                    ? viewersResult.getNext_max_id()
//                    : null;
//        } while (next != null);
//
//        return viewers.stream()
//                .map(InstagramUser::getUsername)
//                .map(this::profile)
//                .collect(Collectors.toList());
//    }
}
