package com.kiselev.enemy.network.instagram.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kiselev.enemy.network.instagram.api.client.InstagramClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.IGThread;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.Inbox;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.feed.Reel;
import com.kiselev.enemy.network.instagram.api.internal2.models.location.Location;
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
import com.kiselev.enemy.network.instagram.api.internal2.requests.locationsearch.LocationSearchRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetCommentsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetLikersRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaListReelMediaViewerRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersUsernameInfoRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectInboxResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectThreadsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserReelsMediaResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUsersResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.locationsearch.LocationSearchResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaGetCommentsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaListReelMediaViewerResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UserResponse;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import com.kiselev.enemy.service.profiler.model.Conversation;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.service.profiler.model.Text;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
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
        List<Profile> followers = followers(profilePk);
        List<Profile> following = following(profilePk);
        return InstagramUtils.friends(followers, following);
    }

    public List<Profile> unfollowers(String profilePk) {
        List<Profile> followers = followers(profilePk);
        List<Profile> following = following(profilePk);
        return InstagramUtils.unfollowers(followers, following);
    }

    public List<Profile> unfollowings(String profilePk) {
        List<Profile> followers = followers(profilePk);
        List<Profile> following = following(profilePk);
        return InstagramUtils.unfollowings(followers, following);
    }

    // TODO: Ugly
    public List<Profile> followers(String profilePk) {
        List<Profile> followers = internalFollowers(profilePk);
        Set<Profile> set = Sets.newHashSet(followers);
        for (int retry = 0; retry < 3; retry++) {
            if (set.size() == followers.size()) {
                return Lists.newArrayList(set);
            } else {
                set.addAll(
                        internalFollowers(profilePk)
                );
            }
        }
        if (set.size() == followers.size()) {
            return Lists.newArrayList(set);
        } else {
            return null;
        }
    }

    public List<Profile> internalFollowers(String profilePk) {
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

        return followers;
    }

    public List<Profile> following(String profilePk) {
        List<Profile> following = internalFollowing(profilePk);
        Set<Profile> set = Sets.newHashSet(following);
        for (int retry = 0; retry < 3; retry++) {
            if (set.size() == following.size()) {
                return Lists.newArrayList(set);
            } else {
                set.addAll(
                        internalFollowing(profilePk)
                );
            }
        }
        if (set.size() == following.size()) {
            return Lists.newArrayList(set);
        } else {
            return null;
        }
    }

    public List<Profile> internalFollowing(String profilePk) {
        List<Profile> following = Lists.newArrayList();

        String next = null;
        do {
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

        Map<Profile, Set<ThreadItem>> history = Maps.newHashMap();
        List<Conversation> conversations = Lists.newArrayList();

        for (IGThread thread : threads) {
            try {
                progress.bar(SocialNetwork.IG, "History messages", threads, thread);

                Profile profile = thread.getUsers().stream()
                        .findFirst()
                        .orElse(User.deleted(thread.getThread_id()));

                Set<ThreadItem> messages = messages(thread.getThread_id());

                conversations.add(
                        Conversation.builder()
                                .id(profile.id())
                                .person(new Person(profile))
                                .texts(messages.stream()
                                        .map(Text::new)
                                        .peek(text -> text.setMine(Objects.equals("1417832744", text.getFrom())))
                                        .collect(Collectors.toList()))
                                .build()
                );

                history.put(profile, messages);

                ProfilingUtils.cache("ig_temporary", conversations);
            } catch (Exception exception) {
                log.warn(exception.getMessage(), exception);
            }
        }

        return history;

//        return threads.stream()
////                .peek(thread -> {
//////                    if (CollectionUtils.isEmpty(thread.getUsers())) {
//////                        String json = ProfilingUtils.json(thread);
//////                        List<Profile> left_users = thread.getLeft_users();
//////                        Profile inviter = thread.getInviter();
//////                        ProfilingUtils.file("thread", thread.getThread_id(), json);
//////                    }
//////                })
//////                .filter(thread -> CollectionUtils.isNotEmpty(thread.getUsers()))
//                .peek(thread -> progress.bar(SocialNetwork.IG, "History messages", threads, thread))
//                .collect(Collectors.toMap(
//                        thread -> thread.getUsers().stream().findFirst().orElse(User.deleted(thread.getThread_id())), // look closely
//                        thread -> messages(thread.getThread_id()),
//                        (first, second) -> {
//                            // TODO: Try to merge
//                            if (first.size() != second.size()) {
//                                throw new RuntimeException("An error happened while merging");
//                            }
//                            return second;
//                        }
//                ));
    }

    public Set<ThreadItem> messages(String threadId) {
        Set<ThreadItem> messages = Sets.newLinkedHashSet();
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

    public String location(Location location) {
        if (isLocationPerfect(location.getName())) {
            return location.getName();
        }
        if (isLocationPerfect(location.getAddress())) {
            return location.getAddress();
        }
        if (isLocationPerfect(location.getShort_name())) {
            return location.getShort_name();
        }

        if (location.getLng() != null && location.getLat() != null) {
            String foundLocation = searchLocation(location.getLng(), location.getLat(), "");

            if (StringUtils.isEmpty(foundLocation)) {
                foundLocation = searchLocation(location.getLng(), location.getLat(), location.getName());
            }

            if (StringUtils.isNotEmpty(foundLocation)) {
                return foundLocation;
            }
        }
        return location.getName();
    }

    private String searchLocation(Double longitude, Double latitude, String name) {
        LocationSearchResponse locationSearchResponse = client.request(
                new LocationSearchRequest(
                        longitude,
                        latitude,
                        name));

        List<String> locations = Lists.newArrayList();

        List<String> names = locationSearchResponse.getVenues().stream()
                .map(Location::getName)
                .filter(StringUtils::isNotEmpty)
                .filter(location -> {
                    Pattern compile = Pattern.compile("[а-яА-Я]");
                    Matcher matcher = compile.matcher(location);
                    return !matcher.find();
                })
                .collect(Collectors.toList());

        List<String> addresses = locationSearchResponse.getVenues().stream()
                .map(Location::getAddress)
                .filter(StringUtils::isNotEmpty)
                .filter(location -> {
                    Pattern compile = Pattern.compile("[а-яА-Я]");
                    Matcher matcher = compile.matcher(location);
                    return !matcher.find();
                })
                .collect(Collectors.toList());

        locations.addAll(names);
        locations.addAll(addresses);

        String perfectLocation = locations.stream()
                .filter(this::isLocationPerfect)
                .findFirst()
                .orElse(null);

        if (perfectLocation != null) {
            return perfectLocation;
        }

        String fineLocation = locations.stream()
                .filter(this::isLocationFine)
                .findFirst()
                .orElse(null);

        if (fineLocation != null) {
            return fineLocation;
        }

        return name;
    }

    private static final Pattern PERFECT_LOCATION = Pattern.compile("^[A-Z][a-z]+\\,\\s[A-Z][a-z]+$");

    private boolean isLocationPerfect(String location) {
        if (location != null) {
            Matcher matcher = PERFECT_LOCATION.matcher(location);
            return matcher.matches();
        }
        return false;
    }

    private static final Pattern FINE_LOCATION = Pattern.compile("^([\\w\\s]+\\,\\s){1,2}[\\w\\s]+$");

    private boolean isLocationFine(String location) {
        if (location != null) {
            Matcher matcher = FINE_LOCATION.matcher(location);
            return matcher.matches();
        }
        return false;
    }
}