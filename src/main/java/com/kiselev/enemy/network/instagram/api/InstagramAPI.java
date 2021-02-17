package com.kiselev.enemy.network.instagram.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kiselev.enemy.network.instagram.api.client.InstagramClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.IGThread;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.Inbox;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.feed.Reel;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.requests.direct.DirectInboxRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.direct.DirectThreadsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedUserReelMediaRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedUserRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsFeedsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetCommentsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetLikersRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaListReelMediaViewerRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersUsernameInfoRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectInboxResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectThreadsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserReelsMediaResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUsersResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaGetCommentsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaListReelMediaViewerResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UserResponse;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramAPI extends ProgressableAPI {

    private final InstagramClient client;

    public List<User> profiles(List<String> usernames) {
        return usernames.stream()
                .peek(username -> progress.bar(SocialNetwork.IG, "Profiles", usernames, username))
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
                return User.deleted(username);
            }
        }
        return null;
    }

    public List<Profile> friends(String profilePk) {
        List<Profile> friends = Lists.newArrayList();

        List<Profile> followers = followers(profilePk);
        List<Profile> following = following(profilePk);

        friends.addAll(followers);
        friends.retainAll(following);

        return friends;
    }

    public List<Profile> followers(String profilePk) {
        List<Profile> followers = Lists.newArrayList();

        String next = null;
        do {
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

        if (followers.size() != followers.stream().distinct().count()) {
            boolean d = true;
        }

        return followers;
    }

    public List<Profile> following(String profilePk) {
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

    public List<TimelineMedia> posts(String profilePk) {
        List<TimelineMedia> posts = Lists.newArrayList();

        String next = null;
        do {
            FeedUserResponse response = client.request(
                    new FeedUserRequest(profilePk, next)
            );

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

    public List<Profile> viewers(String storyId) {
        List<Profile> viewers = Lists.newArrayList();

        String next = null;
        do {

            MediaListReelMediaViewerResponse response = client.request(
                    new MediaListReelMediaViewerRequest(storyId,
                            next)
            );

            if (response != null) {
                viewers.addAll(
                        response.getUsers()
                );
            }

            next = response != null
                    ? response.getNext_max_id()
                    : null;
        } while (next != null);

        return viewers;
    }

    public Map<Profile, Set<ThreadItem>> history() {
        List<IGThread> threads = Lists.newArrayList();
        String cursor = null;

        do {
            DirectInboxResponse response = client.request(
                    new DirectInboxRequest()
                            .cursor(cursor)
                            .limit(100));

            Inbox inbox = response.getInbox();
            threads.addAll(inbox.getThreads());
            cursor = inbox.getOldest_cursor();

        } while (cursor != null);

        return threads.stream()
//                .peek(thread -> {
////                    if (CollectionUtils.isEmpty(thread.getUsers())) {
////                        String json = ProfilingUtils.json(thread);
////                        List<Profile> left_users = thread.getLeft_users();
////                        Profile inviter = thread.getInviter();
////                        ProfilingUtils.file("thread", thread.getThread_id(), json);
////                    }
////                })
////                .filter(thread -> CollectionUtils.isNotEmpty(thread.getUsers()))
                .peek(thread -> progress.bar(SocialNetwork.IG, "History messages", threads, thread))
                .collect(Collectors.toMap(
                        thread -> thread.getUsers().stream().findFirst().orElse(User.deleted(thread.getThread_id())), // look closely
                        thread -> messages(thread.getThread_id()),
                        (first, second) -> {
                            // TODO: Try to merge
                            return second;
                        }
                ));
    }

    public Set<ThreadItem> messages(String threadId) {
        Set<ThreadItem> messages = Sets.newHashSet();
        String cursor = null;

        do {
            DirectThreadsResponse response = client.request(
                    new DirectThreadsRequest(threadId, cursor));

            IGThread thread = response.getThread();
            messages.addAll(thread.getItems());

            cursor = thread.getOldest_cursor();
        } while (cursor != null);

        return messages;
    }
}
