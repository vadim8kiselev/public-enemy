package com.kiselev.enemy.network.telegram.api.bot.command.identifier;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.utils.TelegramUtils;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.model.Zodiac;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.response.SendResponse;
import com.vk.api.sdk.objects.likes.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils.*;
import static com.kiselev.enemy.utils.flow.model.SocialNetwork.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentifierCommand extends ProgressableAPI implements TelegramCommand {

    public static final Integer LIMIT = 10;

    private final PublicEnemyService publicEnemy;

    @Override
    public boolean is(Update update) {
        Integer requestId = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::from)
                .map(User::id)
                .orElse(null);

        String request = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::text)
                .orElse(null);

        log.info("Profile information for identifier {} and request {}", requestId, request);

        return true;
    }

    @Override
    public void execute(Update update, String... args) {
        Integer requestId = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::from)
                .map(User::id)
                .orElse(null);

        String request = Optional.ofNullable(update)
                .map(Update::message)
                .map(Message::text)
                .orElse(null);

        String vkId = ProfilingUtils.identifier(VK, request);
        if (vkId != null) {
            TelegramUtils.log(VK, "Recognized vk identifier: " + vkId);
            vk(requestId, vkId);
            return;
        }

//        String vkId = ProfilingUtils.identifier(SocialNetwork.VK, request);
//        if (vkId != null) {
//
//            List<com.kiselev.enemy.network.vk.api.model.Profile> friends =
//                    publicEnemy.vk().service().api().friends(
//                            publicEnemy.vk().me().id()
//                    );
//
//            for (com.kiselev.enemy.network.vk.api.model.Profile friend : friends) {
//                progress.bar(SocialNetwork.VK, "Friends", friends, friend);
//                vkId = friend.id();
//                String vkResponse = vk(vkId);
//                publicEnemy.tg().send(requestId, TelegramMessage.text(vkResponse));
//            }
//            return;
//        }

        String igId = ProfilingUtils.identifier(IG, request);
        if (igId != null) {
            TelegramUtils.log(IG, "Recognized ig identifier: " + igId);
            ig(requestId, igId);
            return;
        }

        publicEnemy.tg().send(requestId, TelegramMessage.text(String.format("Request %s is not recognized", request)));
    }

    // Main
    private void vk(Integer requestId, String identifier) {
        VKProfile profile = new VKProfile(publicEnemy.vk().service().api().profile(identifier));
        if (profile.username() != null) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk profile: " + profile.username());
        } else {
            TelegramUtils.log(VK, "[" + identifier + "] No vk profile");
            publicEnemy.tg().send(
                    requestId,
                    TelegramMessage.raw("Profile by identifier [" + identifier + "] is not found or private")
            );
            return;
        }

        List<Consumer<VKProfile>> vk = Lists.newArrayList(
                internalProfile -> vk(requestId, internalProfile),
                internalProfile -> ig(requestId, internalProfile)
        );

        vk.parallelStream()
                .forEach(function -> function.accept(profile));
    }

    // Main
    private void ig(Integer requestId, String identifier) {
        InstagramProfile profile = new InstagramProfile(publicEnemy.ig().service().api().profile(identifier));
        if (profile.username() != null) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded instagram profile: " + profile.username());
        } else {
            TelegramUtils.log(IG, "[" + identifier + "] No instagram profile");
            publicEnemy.tg().send(
                    requestId,
                    TelegramMessage.raw("Profile by identifier [" + identifier + "] is not found or private")
            );
            return;
        }

        List<Consumer<InstagramProfile>> ig = Lists.newArrayList(
                internalProfile -> ig(requestId, internalProfile),
                internalProfile -> vk(requestId, internalProfile)
        );

        ig.parallelStream()
                .forEach(function -> function.accept(profile));
    }

    // Profiling
    private void ig(Integer requestId, VKProfile profile) {
        InstagramProfile foundProfile = null;

        String instagram = profile.instagram();

        if (instagram != null) {
            foundProfile = new InstagramProfile(publicEnemy.ig().service().api().profile(instagram));
        }

        String username = profile.username();
        if (username != null) {
            com.kiselev.enemy.network.instagram.api.internal2.models.user.User user =
                    publicEnemy.ig().service().api().profile(username);

            if (user != null && user.isActive()) {
                InstagramProfile instagramProfile = new InstagramProfile(user);

                boolean hasSameFullName = Objects.equals(profile.fullName(), instagramProfile.fullName());

                boolean hasSamePhoneNumber = profile.phone() != null
                        && profile.phone().equals(instagramProfile.public_phone_number());

                boolean hasBackReference = instagramProfile.vk() != null
                        && (instagramProfile.vk().contains(profile.username())
                        || instagramProfile.vk().contains(profile.id()));

                if (hasSameFullName || hasSamePhoneNumber || hasBackReference) {
                    foundProfile = instagramProfile;
                }
            }
        }

        ig(requestId, foundProfile);
    }

    // Profiling
    private void vk(Integer requestId, InstagramProfile profile) {
        VKProfile foundProfile = null;

        String vk = profile.vk();

        if (vk != null) {
            foundProfile = new VKProfile(publicEnemy.vk().service().api().profile(vk));
        }

        vk(requestId, foundProfile);
    }

    // Content
    private void vk(Integer requestId, VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        if (profile != null) {
            SendResponse response = publicEnemy.tg().send(requestId, TelegramMessage.text("\n*VK:*\n..."));

            profile.friends(
                    publicEnemy.vk().service().api().friends(profile.id()).stream()
                            .map(VKProfile::new)
                            .collect(Collectors.toList()));
            if (profile.friends() != null) {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk friends: " + profile.friends().size() + " friends");
            } else {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] No vk friends");
            }

            profile.photos(
                    publicEnemy.vk().service().photos(profile.id(), false)
            );
            if (profile.photos() != null) {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk photos: " + profile.photos().size() + " photos");
            } else {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] No vk photos");
            }

            profile.posts(
                    publicEnemy.vk().service().posts(profile.id(), false)
            );
            if (profile.posts() != null) {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk posts: " + profile.posts().size() + " posts");
            } else {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] No vk posts");
            }

//        profile.area(
//                profile.friends().stream()
//                        .filter(VKProfile::isActive)
//                        .peek(friend -> friend.friends(
//                                publicEnemy.vk().service().api().friends(friend.id()).stream()
//                                        .map(VKProfile::new)
//                                        .collect(Collectors.toList())))
//                        .collect(Collectors.toMap(
//                                friend -> friend,
//                                VKProfile::friends)));
//        TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk area: " + profile.area().size() + " people");

            List<Function<VKProfile, List<String>>> vk = Lists.newArrayList(
                    this::vkInformation,
                    this::vkContacts,
                    this::vkLife,
                    this::vkStatistics,
                    this::vkLikes,
                    this::vkHiders,
                    this::vkRelatives,
                    this::vkPredictions
            );

            for (Function<VKProfile, List<String>> function : vk) {
                publicEnemy.tg().service().api().bot().sendTyping(requestId);

                List<String> page = function.apply(profile);
                if (CollectionUtils.isNotEmpty(page)) {
                    messages.addAll(
                            page
                    );

                    publicEnemy.tg().update(requestId,
                            response.message().messageId(),
                            TelegramMessage.text(card("\n*VK:*", messages, false)));
                }
            }

            publicEnemy.tg().update(requestId,
                    response.message().messageId(),
                    TelegramMessage.text(card("\n*VK:*", messages, true)));
        }
    }

    // Content
    private void ig(Integer requestId, InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        if (profile != null) {
            SendResponse response = publicEnemy.tg().send(requestId, TelegramMessage.text("\n*Instagram:*\n..."));

            profile.posts(
                    publicEnemy.ig().service().api().posts(profile.id()).stream()
                            .map(InstagramPost::new)
                            .collect(Collectors.toList()));
            if (profile.posts() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig posts: " + profile.posts().size() + " posts");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig posts");
            }

            List<Profile> followers = publicEnemy.ig().service().api().followers(profile.id());
            if (followers != null) {
                profile.followers(
                        followers.stream()
                                .map(InstagramProfile::new)
                                .collect(Collectors.toList()));
            }
            if (profile.followers() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig followers: " + profile.followers().size() + " followers");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig followers");
            }

            List<Profile> following = publicEnemy.ig().service().api().following(profile.id());
            if (following != null) {
                profile.following(following.stream()
                        .map(InstagramProfile::new)
                        .collect(Collectors.toList()));
            }
            if (profile.following() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig following: " + profile.following().size() + " following");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig following");
            }


            profile.friends(
                    InstagramUtils.friends(profile)
            );
            if (profile.friends() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig friends: " + profile.friends().size() + " friends");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig friends");
            }

            profile.unfollowers(
                    InstagramUtils.unfollowers(profile)
            );
            if (profile.unfollowers() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig unfollowers: " + profile.unfollowers().size() + " unfollowers");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig unfollowers");
            }

            profile.unfollowings(
                    InstagramUtils.unfollowings(profile)
            );
            if (profile.unfollowings() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig unfollowings: " + profile.unfollowings().size() + " unfollowings");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig unfollowings");
            }

            List<Function<InstagramProfile, List<String>>> ig = Lists.newArrayList(
                    this::igInformation,
                    this::igContacts,
                    this::igPeople,
                    this::igStatistics,
                    this::igLikes,
                    this::igTags,
                    this::igHashTags,
                    this::igLocations,
                    this::igPredictions
            );

            for (Function<InstagramProfile, List<String>> function : ig) {
                publicEnemy.tg().service().api().bot().sendTyping(requestId);

                List<String> page = function.apply(profile);
                if (CollectionUtils.isNotEmpty(page)) {
                    messages.addAll(
                            function.apply(profile)
                    );

                    publicEnemy.tg().update(requestId,
                            response.message().messageId(),
                            TelegramMessage.text(card("\n*Instagram:*", messages, false)));
                }
            }

            publicEnemy.tg().update(requestId,
                    response.message().messageId(),
                    TelegramMessage.text(card("\n*Instagram:*", messages, true)));
        }
    }

    private List<String> vkInformation(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> information = Lists.newArrayList();
        information.add(message("👤 Name", profile.name()));
        information.add(message("📝 Status", profile.status()));
        information.add(message("🚻 Sex", profile.sex()));

        String age = profile.age();
        if (age != null) {
            information.add(message("🔞 Age", age));
        } else {
            String hiddenAge = publicEnemy.vk().service().searchAge(profile);
            information.add(message("🔞 Age", hiddenAge, "(hidden)"));
        }

        String birthDate = profile.birthDate();
        if (birthDate != null) {
            if (age != null) {
                information.add(message("📅 Birth date", birthDate));
            } else {
                String hiddenBirthday = publicEnemy.vk().service().searchBirthDate(profile);
                if (hiddenBirthday != null) {
                    information.add(message("📅 Birth date", birthDate, "(Hidden: " + hiddenBirthday + ")"));
                } else {
                    information.add(message("📅 Birth date", birthDate));
                }
            }
        } else {
            String hiddenBirthday = publicEnemy.vk().service().searchBirthDate(profile);
            information.add(message("📅 Birth date", hiddenBirthday, "(hidden)"));
        }

        if (birthDate != null) {
            List<Zodiac> zodiacs = VKUtils.zodiacs(birthDate);
            if (zodiacs.size() > 1) {
                Iterator<Zodiac> iterator = zodiacs.iterator();
                Zodiac zodiac1 = iterator.next();
                Zodiac zodiac2 = iterator.next();

                information.add(message("🔄 Zodiac", "Cusp: " + zodiac1.title() + " - " + zodiac2.title()));
            } else {
                Zodiac zodiac = zodiacs.iterator().next();
                information.add(message(zodiac.sign() + " Zodiac", zodiac.title()));
            }
        }

        information.add(message("🏙 City", profile.city()));
        information.add(message("🌎 Country", profile.country()));
        information.add(message("🏠 Home town", profile.homeTown()));

        if (VKUtils.isNotEmpty(information)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded information");
            messages.addAll(information);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No information");
        }

        return messages;
    }

    private List<String> vkContacts(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> contacts = Lists.newArrayList();
        contacts.add(message("📞 Phone", profile.phone()));
        // TODO: Look for email in VK
        contacts.add(link("📘 Facebook", SocialNetwork.FB, profile.fullName(), profile.facebook()));
        contacts.add(link("✈️ Telegram", TG, profile.telegram()));
        contacts.add(link("📷 Instagram", IG, profile.instagram()));
        contacts.add(link("🐦 Twitter", SocialNetwork.TW, profile.twitter()));
        contacts.add(message("📟 Skype", profile.skype()));

        if (VKUtils.isNotEmpty(contacts)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded contacts");
            messages.add("\nContacts:");
            messages.addAll(contacts);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No contacts");
        }

        return messages;
    }

    private List<String> vkLife(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> life = Lists.newArrayList();
        life.add(messages("🏫 School", profile.school()));
        life.add(messages("🏢 University", profile.university()));
        life.add(messages("🏦 Job", profile.job()));

        if (VKUtils.isNotEmpty(life)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded life");
            messages.add("\nLife:");
            messages.addAll(life);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No life");
        }

        return messages;
    }

    private List<String> vkStatistics(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

//        List<String> statistics = Lists.newArrayList();
//        List<Post> posts = profile.posts();
//        List<Photo> photos = profile.photos();
//
//        int totalNumberOfPosts = posts.size() + photos.size();
//        int totalNumberOfLikes = posts.stream()
//                .mapToInt(post -> post.likesInfo().getCount())
//                .sum()
//                +
//                photos.stream()
//                        .mapToInt(photo -> photo.likesInfo().getCount())
//                        .sum();
//        int totalNumberOfCommentaries = posts.stream()
//                .mapToInt(post -> post.commentsInfo().getCount())
//                .sum();
//
//        Map<Integer, List<Post>> dateMap = posts.stream()
//                .sorted(Comparator.comparing(Post::date, Comparator.reverseOrder()))
//                .collect(Collectors.groupingBy(
//                        post -> post.date().getYear(),
//                        LinkedHashMap::new,
//                        Collectors.toList()));
//
//        // TODO: Bold rows for scores
//
//        for (Map.Entry<Integer, List<InstagramPost>> entry : dateMap.entrySet()) {
//            Integer year = entry.getKey();
//            List<InstagramPost> yearPosts = entry.getValue();
//
//            int yearLikes = yearPosts.stream()
//                    .mapToInt(InstagramPost::likeCount)
//                    .sum();
//            int yearCommentaries = yearPosts.stream()
//                    .mapToInt(InstagramPost::commentariesCount)
//                    .sum();
//
//            statistics.add("🗓 " + year + ":");
//            statistics.add(message(" - Posts", statistics(yearPosts.size(), totalNumberOfPosts)));
//            statistics.add(message(" - Types", null));
//            statistics.add(message(" - Likes", statistics(yearLikes, totalNumberOfLikes)));
//            statistics.add(message(" - Comments", statistics(yearCommentaries, totalNumberOfCommentaries)));
//        }
//
//        if (VKUtils.isNotEmpty(statistics)) {
//            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded statistics");
//            messages.add("\nStatistics:");
//            messages.addAll(statistics);
//        } else {
//            TelegramUtils.log(IG, "[" + profile.identifier() + "] No statistics");
//        }

        return messages;
    }

    private List<String> vkLikes(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<VKProfile> vkLikes = Lists.newArrayList();

        List<Photo> photos = profile.photos();
        List<VKProfile> photoslikes = photos.stream()
                .map(photo -> publicEnemy.vk().service().api().likes(profile.id(), photo.id(), Type.PHOTO))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(VKProfile::new)
                .collect(Collectors.toList());
        vkLikes.addAll(photoslikes);

        List<Post> posts = profile.posts();
        List<VKProfile> postslikes = posts.stream()
                .map(photo -> publicEnemy.vk().service().api().likes(profile.id(), photo.id(), Type.POST))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(VKProfile::new)
                .collect(Collectors.toList());
        vkLikes.addAll(postslikes);

        int totalNumberOfItems = photos.size() + posts.size();

        boolean isAvailableToGetAll = totalNumberOfItems < 200;

        Map<VKProfile, Long> vkLikersHeatMap = vkLikes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long maxRate = vkLikersHeatMap.values().stream()
                .max(Long::compareTo)
                .orElse(null);

        long limit = Math.max(
                vkLikersHeatMap.values().stream()
                        .filter(heat -> Objects.equals(heat, maxRate))
                        .count() + 1,
                LIMIT);

        List<String> likes = vkLikersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<VKProfile, Long>comparingByValue().reversed())
                .limit(limit)
                .map(liker -> like("💙", liker.getKey().name(), liker.getValue(), totalNumberOfItems))
                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(likes)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded likes");
            if (isAvailableToGetAll) {
                messages.add("\nLikes:");
            } else {
                messages.add("\nLikes for the last year:");
            }
            messages.addAll(likes);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No likes");
        }

        return messages;
    }

    private List<String> vkHiders(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> hiders = Lists.newArrayList();
        Map<VKProfile, List<VKProfile>> area = profile.area();
        if (area != null) {
            for (Map.Entry<VKProfile, List<VKProfile>> hider : area.entrySet()) {
                VKProfile friend = hider.getKey();
                List<VKProfile> friends = hider.getValue();
                // TODO: What if all the friends are hidden
                if (CollectionUtils.isNotEmpty(friends) && !friends.contains(profile)) {
                    hiders.add(message("😶 Friend", friend.name()));
                }
            }
        }

        if (VKUtils.isNotEmpty(hiders)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded hidden friends");
            messages.add("\nHidden by friends:");
            messages.addAll(hiders);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No hidden friends");
        }

        return messages;
    }

    private List<String> vkRelatives(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> relatives = Lists.newArrayList();

        List<VKProfile> people = publicEnemy.vk().service().relatives(profile.profile());
        for (VKProfile relative : people) {
            if (relative.isActive() && VKUtils.areFamilyMembers(profile.lastName(), relative.lastName())) { // TODO: Recheck
                relatives.add(message("👥 Relative", relative.name()));
            }
        }

        List<VKProfile> friends = profile.friends();
        for (VKProfile friend : friends) {
            if (friend.isActive() && VKUtils.areFamilyMembers(profile.lastName(), friend.lastName())) { // TODO: Recheck
                relatives.add(message("👥 Relative", friend.name()));
            }
        }

        if (VKUtils.isNotEmpty(relatives)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded relatives");
            messages.add("\nRelatives:");
            messages.addAll(relatives);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No relatives");
        }

        return messages;
    }

    private List<String> vkPredictions(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<VKProfile> friends = profile.friends();

        for (VKProfile friend : friends) {
            if (CollectionUtils.isNotEmpty(friend.friends())) {
                if (friend.age() == null) {
                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::age, friend.friends());
                    if (prediction != null) {
                        friend.age(prediction.value());
                    }
                }
                if (friend.country() == null) {
                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::country, friend.friends());
                    if (prediction != null) {
                        friend.country(prediction.value());
                    }
                }
                if (friend.city() == null) {
                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::city, friend.friends());
                    if (prediction != null) {
                        friend.city(prediction.value());
                    }
                }
                if (friend.homeTown() == null) {
                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::homeTown, friend.friends());
                    if (prediction != null) {
                        friend.homeTown(prediction.value());
                    }
                }
                if (friend.school() == null) {
                    List<String> schools = friend.friends().stream()
                            .map(VKProfile::school)
                            .flatMap(List::stream)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    Prediction<String> prediction = AnalyticsUtils.predict(schools);
                    if (prediction != null) {
                        friend.school(Collections.singletonList(
                                prediction.value()
                        ));
                    }
                }
                if (friend.university() == null) {
                    List<String> universities = friend.friends().stream()
                            .map(VKProfile::university)
                            .flatMap(List::stream)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    Prediction<String> prediction = AnalyticsUtils.predict(universities);
                    if (prediction != null) {
                        friend.university(Collections.singletonList(
                                prediction.value()
                        ));
                    }
                }
                if (friend.job() == null) {
                    List<String> jobs = friend.friends().stream()
                            .map(VKProfile::job)
                            .flatMap(List::stream)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    Prediction<String> prediction = AnalyticsUtils.predict(jobs);
                    if (prediction != null) {
                        friend.job(Collections.singletonList(
                                prediction.value()
                        ));
                    }
                }
            }
        }

        List<String> predictions = Lists.newArrayList();
        predictions.add(prediction("🔞 Age", VKProfile::age, friends));
        predictions.add(prediction("🌎 Country", VKProfile::country, friends));
        predictions.add(prediction("🏙 City", VKProfile::city, friends));
        predictions.add(prediction("🏠 Home town", VKProfile::homeTown, friends));
        predictions.add(IdentifierUtils.predictions("🏫 School", VKProfile::school, friends));
        predictions.add(IdentifierUtils.predictions("🏢 University", VKProfile::university, friends));
        predictions.add(IdentifierUtils.predictions("🏦 Job", VKProfile::job, friends));
        if (VKUtils.isNotEmpty(predictions)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Prepared predictions");
            messages.add("\nPredictions:");
            messages.addAll(predictions);
        } else {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] No predictions]");
        }

        return messages;
    }

    //

    private List<String> igInformation(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> information = Lists.newArrayList();

        information.add(message("👤 Username", profile.name()));
        information.add(message("💬 Full name", profile.fullName()));
//        information.add(message("🗣 Type", profile.profileType()));
        information.add(message("📷 Category", profile.category()));
        information.add(message(
                "📅 Active since",
                profile.posts().stream()
                        .min(Comparator.comparing(InstagramPost::date))
                        .map(InstagramPost::date)
                        .map(InstagramUtils::dateToString)
                        .orElse(null)
        ));

        if (StringUtils.isNotEmpty(profile.biography())) {
            information.add("📝 Biography:");
            information.add(profile.biography());
        }

        if (VKUtils.isNotEmpty(information)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded information");
            messages.addAll(information);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No information");
        }

        return messages;
    }

    private List<String> igContacts(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> contacts = Lists.newArrayList();
        contacts.add(message("📞 Phone", profile.public_phone_number()));
        contacts.add(message("📧 Email", profile.public_email()));
        contacts.add(link("🐶 VK", VK, profile.vk()));
        contacts.add(link("✈️ Telegram", TG, profile.telegram()));

        if (ProfilingUtils.identifier(VK, profile.external_url()) == null
                && ProfilingUtils.identifier(TG, profile.external_url()) == null) {
            contacts.add(message("🌐 Website", profile.external_url()));
        }

        if (VKUtils.isNotEmpty(contacts)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded contacts");
            messages.add("\nContacts:");
            messages.addAll(contacts);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No contacts");
        }

        return messages;
    }

    private List<String> igPeople(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> people = Lists.newArrayList();
        List<InstagramProfile> following = profile.following();
        if (CollectionUtils.isNotEmpty(following)) {
            people.add(message("➡️ Following", following.size(), "people"));
        }

        List<InstagramProfile> followers = profile.followers();
        if (CollectionUtils.isNotEmpty(followers)) {
            people.add(message("⬅️ Followers", followers.size(), "people"));
        }

        List<InstagramProfile> friends = profile.friends();
        if (CollectionUtils.isNotEmpty(friends)) {
            Set<InstagramProfile> tags = profile.posts().stream()
                    .map(InstagramPost::tags)
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

            List<InstagramProfile> bestFriends = friends.stream()
                    .filter(tags::contains)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(bestFriends)) {
                if (bestFriends.size() <= LIMIT) {
                    people.add("Best friends:");
                    for (InstagramProfile bestFriend : bestFriends) {
                        people.add(message("⭐️ Best friend", bestFriend.name()));
                    }
                } else {
                    people.add(message("⭐️ Best friends", bestFriends.size(), "people"));
                }
            }

            if (friends.size() <= LIMIT) {
                people.add("Friends:");
                for (InstagramProfile friend : friends) {
                    people.add(message("🤝 Friend", friend.name()));
                }
            } else {
                people.add(message("🤝 Friends", friends.size(), "people"));
            }
        }

        List<InstagramProfile> unfollowings = profile.unfollowings();
        if (CollectionUtils.isNotEmpty(unfollowings)) {
            if (unfollowings.size() <= LIMIT) {
                people.add("Unfollowings:");
                for (InstagramProfile unfollowing : unfollowings) {
                    people.add(message("✋ Unfollowing", unfollowing.name()));
                }
            } else {
                people.add(message("✋ Unfollowings", unfollowings.size(), "people"));
            }
        }

        List<InstagramProfile> unfollowers = profile.unfollowers();
        if (CollectionUtils.isNotEmpty(unfollowers)) {
            if (unfollowers.size() <= LIMIT) {
                people.add("Unfollowers:");
                for (InstagramProfile unfollower : unfollowers) {
                    people.add(message("🤚 Unfollower", unfollower.name()));
                }
            } else {
                people.add(message("🤚 Unfollowers", unfollowers.size(), "people"));
            }
        }

        if (VKUtils.isNotEmpty(people)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded people");
            messages.add("\nPeople:");
            messages.addAll(people);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No people");
        }

        return messages;
    }

    private List<String> igStatistics(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> statistics = Lists.newArrayList();
        List<InstagramPost> posts = profile.posts();
        int totalNumberOfPosts = posts.size();
        int totalNumberOfLikes = posts.stream()
                .mapToInt(InstagramPost::likeCount)
                .sum();
        int totalNumberOfCommentaries = posts.stream()
                .mapToInt(InstagramPost::commentariesCount)
                .sum();

        Map<Integer, List<InstagramPost>> dateMap = posts.stream()
                .sorted(Comparator.comparing(InstagramPost::date, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(
                        post -> post.date().getYear(),
                        LinkedHashMap::new,
                        Collectors.toList()));

        // TODO: Bold rows for scores

        for (Map.Entry<Integer, List<InstagramPost>> entry : dateMap.entrySet()) {
            Integer year = entry.getKey();
            List<InstagramPost> yearPosts = entry.getValue();

            int yearLikes = yearPosts.stream()
                    .mapToInt(InstagramPost::likeCount)
                    .sum();
            int yearCommentaries = yearPosts.stream()
                    .mapToInt(InstagramPost::commentariesCount)
                    .sum();

            statistics.add("🗓 " + year + ":");
            statistics.add(message(" - Posts", statistics(yearPosts.size(), totalNumberOfPosts)));
            statistics.add(message(" - Types", null));
            statistics.add(message(" - Likes", statistics(yearLikes, totalNumberOfLikes)));
            statistics.add(message(" - Comments", statistics(yearCommentaries, totalNumberOfCommentaries)));
        }

        if (VKUtils.isNotEmpty(statistics)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded statistics");
            messages.add("\nStatistics:");
            messages.addAll(statistics);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No statistics");
        }

        return messages;
    }

    private List<String> igLikes(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<InstagramPost> posts = profile.posts();

        boolean isAvaiableToGetAll = posts.size() < 100;

        List<InstagramPost> latestInstagramPosts = posts.stream()
                .filter(post -> isAvaiableToGetAll || post.date().isAfter(LocalDateTime.now().minusYears(1)))
                .collect(Collectors.toList());

        int totalNumberOfItems = latestInstagramPosts.size();

        List<Profile> igLikes = latestInstagramPosts.stream()
                .map(post -> publicEnemy.ig().service().api().likes(post.id()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Map<Profile, Long> igLikersHeatMap = igLikes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long maxRate = igLikersHeatMap.values().stream()
                .max(Long::compareTo)
                .orElse(null);

        long limit = Math.max(
                igLikersHeatMap.values().stream()
                        .filter(heat -> Objects.equals(heat, maxRate))
                        .count() + 1,
                LIMIT);

        List<String> likes = igLikersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<Profile, Long>comparingByValue().reversed())
                .limit(limit)
                .map(liker -> like("🧡", liker.getKey().name(), liker.getValue(), totalNumberOfItems))
                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(likes)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded likes");
            if (isAvaiableToGetAll) {
                messages.add("\nLikes:");
            } else {
                messages.add("\nLikes for the last year:");
            }
            messages.addAll(likes);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No likes");
        }

        return messages;
    }

    private List<String> igTags(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<InstagramPost> posts = profile.posts();
        List<InstagramProfile> topTags = AnalyticsUtils.topObjects(
                posts.stream()
                        .map(InstagramPost::tags)
                        .filter(Objects::nonNull)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()),
                LIMIT);

        List<String> tags = topTags.stream()
                .map(tag -> {
                    List<InstagramPost> tagPosts = posts.stream()
                            .filter(post -> post.tags() != null && post.tags().contains(tag))
                            .collect(Collectors.toList());

                    return Statistics.of(
                            "🏷 " + tag.name(),
                            label(tagPosts.size(), "tag"),
                            tagPosts.stream()
                                    .min(Comparator.comparing(InstagramPost::date))
                                    .map(InstagramPost::date)
                                    .orElse(null),
                            tagPosts.stream()
                                    .max(Comparator.comparing(InstagramPost::date))
                                    .map(InstagramPost::date)
                                    .orElse(null)
                    );
                })
                .sorted(Comparator.comparing(Statistics::to, Comparator.reverseOrder()))
                .map(Statistics::toString)

                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(tags)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded tags");
            messages.add("\nTags:");
            messages.addAll(tags);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No tags");
        }

        return messages;
    }

    private List<String> igHashTags(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<InstagramPost> posts = profile.posts();
        List<String> topHashTags = AnalyticsUtils.topObjects(
                posts.stream()
                        .map(InstagramPost::hashTags)
                        .filter(Objects::nonNull)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()),
                LIMIT);

        List<String> hashTags = topHashTags.stream()
                .map(hashTag -> {
                    List<InstagramPost> hashTagPosts = posts.stream()
                            .filter(post -> post.hashTags() != null && post.hashTags().contains(hashTag))
                            .collect(Collectors.toList());

                    return Statistics.of(
                            "🔗 " + hashTag,
                            label(hashTagPosts.size(), "hash tag"),
                            hashTagPosts.stream()
                                    .min(Comparator.comparing(InstagramPost::date))
                                    .map(InstagramPost::date)
                                    .orElse(null),
                            hashTagPosts.stream()
                                    .max(Comparator.comparing(InstagramPost::date))
                                    .map(InstagramPost::date)
                                    .orElse(null)
                    );
                })
                .sorted(Comparator.comparing(Statistics::to, Comparator.reverseOrder()))
                .map(Statistics::toString)

                .collect(Collectors.toList());

//        Map<String, Long> igHashTagsHeatMap = profile.posts().stream()
//                .map(InstagramPost::hashTags)
//                .filter(Objects::nonNull)
//                .flatMap(List::stream)
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//
//        List<String> hashTags = igHashTagsHeatMap.entrySet().stream()
//                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
//                .limit(LIMIT)
//                .map(this::hashTag)
//                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(hashTags)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded hash tags");
            messages.add("\nHash tags:");
            messages.addAll(hashTags);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No hash tags");
        }

        return messages;
    }

    private List<String> igLocations(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<InstagramPost> posts = profile.posts();
        List<String> topLocations = AnalyticsUtils.topObjects(
                posts.stream()
                        .map(InstagramPost::location)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()),
                LIMIT);

        List<String> locations = topLocations.stream()
                .map(location -> {
                    List<InstagramPost> locationPosts = posts.stream()
                            .filter(post -> location.equals(post.location()))
                            .collect(Collectors.toList());

                    return Statistics.of(
                            "📍 " + location,
                            label(locationPosts.size(), "photo"),
                            locationPosts.stream()
                                    .min(Comparator.comparing(InstagramPost::date))
                                    .map(InstagramPost::date)
                                    .orElse(null),
                            locationPosts.stream()
                                    .max(Comparator.comparing(InstagramPost::date))
                                    .map(InstagramPost::date)
                                    .orElse(null)
                    );
                })
                .sorted(Comparator.comparing(Statistics::to, Comparator.reverseOrder()))
                .map(Statistics::toString)
                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(locations)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded locations");
            messages.add("\nLocations:");
            messages.addAll(locations);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No locations");
        }

        return messages;
    }

    private List<String> igPredictions(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> predictions = Lists.newArrayList();

        List<InstagramProfile> friends = profile.friends();
        if (friends != null) {
            for (InstagramProfile friend : friends) {
                if (CollectionUtils.isNotEmpty(friend.friends())) {
                    if (friend.location() == null) {
                        Prediction<String> prediction = AnalyticsUtils.predict(InstagramProfile::location, friend.friends());
                        if (prediction != null) {
                            friend.location(prediction.value());
                        }
                    }
                }
            }

            predictions.add(prediction("📍 Location", InstagramProfile::location, friends));
        }

        if (VKUtils.isNotEmpty(predictions)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded predictions");
            messages.add("\nPredictions:");
            messages.addAll(predictions);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No predictions");
        }

        return messages;
    }

    //

    private String statistics(Integer current, Integer total) {
        return current + " / " + total + " (" + ((current * 100) / total) + "%)";
    }

    private String like(String emoji, String name, Long number, Integer totalNumberOfItems) {
        return message(
                emoji + " " + name,
                (number + " / " + totalNumberOfItems) + (number == 1 ? " like" : " likes"));
    }

    private String label(Integer number, String label) {
        return number + (number == 1 ? " " + label : " " + label + "s");
    }

    @Data
    @Accessors(fluent = true)
    @AllArgsConstructor(staticName = "of")
    private static class Statistics {
        private String name;
        private String label;
        private LocalDateTime from;
        private LocalDateTime to;

        @Override
        public String toString() {
            String from = Optional.of(this.from())
                    .map(date -> String.format("%02d", date.getMonthValue()) + "." + date.getYear())
                    .get();

            String to = Optional.of(this.to())
                    .map(date -> String.format("%02d", date.getMonthValue()) + "." + date.getYear())
                    .get();

            if (Objects.equals(from, to)) {
                return this.name() + " (" + this.label() + ": " + from + ")";
            } else {
                return this.name() + " (" + this.label() + ": " + from + " - " + to + ")";
            }
        }
    }
}
