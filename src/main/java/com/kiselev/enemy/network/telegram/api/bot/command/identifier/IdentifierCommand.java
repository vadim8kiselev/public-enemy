package com.kiselev.enemy.network.telegram.api.bot.command.identifier;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.UserTags;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.instagram.service.InstagramService;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.telegram.utils.TelegramUtils;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.kiselev.enemy.network.vk.api.model.Post;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.model.Zodiac;
import com.kiselev.enemy.network.vk.service.VKService;
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
import com.vk.api.sdk.objects.likes.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils.*;
import static com.kiselev.enemy.utils.flow.model.SocialNetwork.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentifierCommand extends ProgressableAPI implements TelegramCommand {

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
            String vkResponse = vk(vkId);
            publicEnemy.tg().send(requestId, TelegramMessage.text(vkResponse));
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
            TelegramUtils.log(VK, "Recognized ig identifier: " + igId);
            String igResponse = ig(igId);
            publicEnemy.tg().send(requestId, TelegramMessage.text(igResponse));
            return;
        }

        publicEnemy.tg().send(requestId, TelegramMessage.text(String.format("Request %s is not recognized", request)));
    }

    // Main
    private String vk(String identifier) {
        VKProfile profile = new VKProfile(publicEnemy.vk().service().api().profile(identifier));
        if (profile.username() != null) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk profile: " + profile.username());
        } else {
            TelegramUtils.log(VK, "[" + identifier + "] No vk profile");
            return "Profile by identifier [" + identifier + "] is not found or private";
        }

        Map<Integer, Function<VKProfile, List<String>>> vk = ImmutableMap
                .<Integer, Function<VKProfile, List<String>>>builder()
                .put(1, this::vk)
                .put(2, this::ig)
                .build();

        List<String> messages = vk.entrySet().parallelStream()
                .map(entry -> entry.getValue().apply(profile))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return messages.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }

    // Main
    private String ig(String identifier) {
        InstagramProfile profile = new InstagramProfile(publicEnemy.ig().service().api().raw().profile(identifier));
        if (profile.username() != null) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded instagram profile: " + profile.username());
        } else {
            TelegramUtils.log(IG, "[" + identifier + "] No instagram profile");
            return "Profile by identifier [" + identifier + "] is not found or private";
        }

        Map<Integer, Function<InstagramProfile, List<String>>> ig = ImmutableMap
                .<Integer, Function<InstagramProfile, List<String>>>builder()
                .put(1, this::ig)
                .put(2, this::vk)
                .build();

        List<String> messages = ig.entrySet().parallelStream()
                .map(entry -> entry.getValue().apply(profile))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return messages.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }

    // Profiling
    private List<String> ig(VKProfile profile) {
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

        return ig(foundProfile);
    }

    // Profiling
    private List<String> vk(InstagramProfile profile) {
        VKProfile foundProfile = null;

        String vk = profile.vk();

        if (vk != null) {
            foundProfile = new VKProfile(publicEnemy.vk().service().api().profile(vk));
        }

        return vk(foundProfile);
    }

    // Content
    private List<String> vk(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        if (profile != null) {
            profile.friends(
                    publicEnemy.vk().service().api().friends(profile.id()).stream()
                            .map(VKProfile::new)
                            .collect(Collectors.toList()));
            if (profile.friends() != null) {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded vk friends: " + profile.friends().size() + " friends");
            } else {
                TelegramUtils.log(VK, "[" + profile.identifier() + "] No vk friends");
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

            Map<Integer, Function<VKProfile, List<String>>> vk = ImmutableMap
                    .<Integer, Function<VKProfile, List<String>>>builder()
                    .put(1, this::vkInformation)
                    .put(2, this::vkContacts)
                    .put(3, this::vkLife)
                    .put(4, this::vkLikes)
                    .put(5, this::vkHiders)
                    .put(6, this::vkRelatives)
                    .put(7, this::vkPredictions)
                    .build();

            List<String> response = vk.entrySet().parallelStream()
                    .map(entry -> entry.getValue().apply(profile))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (VKUtils.isNotEmpty(response)) {
                messages.add("\n*VK:*");
                messages.addAll(response);
            }
        }

        return messages;
    }

    // Content
    private List<String> ig(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        if (profile != null) {
            profile.posts(
                    publicEnemy.ig().service().api().raw().posts(profile.id()).stream()
                            .map(InstagramPost::new)
                            .collect(Collectors.toList()));
            if (profile.posts() != null) {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded ig posts: " + profile.posts().size() + " posts");
            } else {
                TelegramUtils.log(IG, "[" + profile.identifier() + "] No ig posts");
            }

            List<Profile> followers = publicEnemy.ig().service().api().raw().followers(profile.id());
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

            List<Profile> following = publicEnemy.ig().service().api().raw().following(profile.id());
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

            Map<Integer, Function<InstagramProfile, List<String>>> ig = ImmutableMap
                    .<Integer, Function<InstagramProfile, List<String>>>builder()
                    .put(1, this::igInformation)
                    .put(2, this::igContacts)
                    .put(3, this::igPeople)
                    .put(4, this::igLikes)
                    .put(5, this::igTags)
                    .put(6, this::igLocations)
                    .put(7, this::igPredictions)
                    .build();

            List<String> response = ig.entrySet().parallelStream()
                    .map(entry -> entry.getValue().apply(profile))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (VKUtils.isNotEmpty(response)) {
                messages.add("\n*Instagram:*");
                messages.addAll(response);
            }
        }

        return messages;
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

    private List<String> vkLikes(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<VKProfile> vkLikes = Lists.newArrayList();

        List<Photo> photos = publicEnemy.vk().service().photos(profile.id(), false);
        List<VKProfile> photoslikes = photos.stream()
                .map(Photo::likes)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        vkLikes.addAll(photoslikes);

        List<Post> posts = publicEnemy.vk().service().posts(profile.id(), false);
        List<VKProfile> postslikes = posts.stream()
                .map(Post::likes)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        vkLikes.addAll(postslikes);

        int totalNumberOfItems = photos.size() + posts.size();

        Map<VKProfile, Long> vkLikersHeatMap = vkLikes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<String> likes = vkLikersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<VKProfile, Long>comparingByValue().reversed())
                .limit(5)
                .map(liker -> like("💙", liker.getKey().name(), liker.getValue(), totalNumberOfItems))
                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(likes)) {
            TelegramUtils.log(VK, "[" + profile.identifier() + "] Downloaded likes");
            messages.add("\nLikes for the last year:");
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

        information.add(message("🗣 Type", profile.profileType()));
        information.add(message("📷 Category", profile.category()));
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
            if (friends.size() <= 5) {
                people.add("\nFriends:");
                for (InstagramProfile friend : friends) {
                    people.add(message("🤝 Friend", friend.name()));
                }
            } else {
                people.add(message("🤝 Friends", friends.size(), "people"));
            }
        }

        List<InstagramProfile> unfollowers = profile.unfollowers();
        if (CollectionUtils.isNotEmpty(unfollowers)) {
            if (unfollowers.size() <= 5) {
                people.add("\nUnfollowers:");
                for (InstagramProfile unfollower : unfollowers) {
                    people.add(message("👐 Unfollower", unfollower.name()));
                }
            } else {
                people.add(message("👐 Unfollowers", unfollowers.size(), "people"));
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

    private List<String> igLikes(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<InstagramPost> latestInstagramPosts = profile.posts().stream()
                .filter(post -> post.date().isAfter(LocalDateTime.now().minusYears(1)))
                .collect(Collectors.toList());

        int totalNumberOfItems = latestInstagramPosts.size();

        List<Profile> igLikes = latestInstagramPosts.stream()
                .map(post -> publicEnemy.ig().service().api().raw().likes(post.id()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Map<Profile, Long> igLikersHeatMap = igLikes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<String> likes = igLikersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<Profile, Long>comparingByValue().reversed())
                .limit(5)
                .map(liker -> like("🧡", liker.getKey().name(), liker.getValue(), totalNumberOfItems))
                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(likes)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded likes");
            messages.add("\nLikes for the last year:");
            messages.addAll(likes);
        } else {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] No likes");
        }

        return messages;
    }

    private List<String> igTags(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        Map<InstagramProfile, Long> igTagsHeatMap = profile.posts().stream()
                .map(InstagramPost::tags)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<String> tags = igTagsHeatMap.entrySet().stream()
                .sorted(Map.Entry.<InstagramProfile, Long>comparingByValue().reversed())
                .limit(5)
                .map(this::tag)
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

    private String tag(Map.Entry<InstagramProfile, Long> entry) {
        String name = entry.getKey().name();
        Long tags = entry.getValue();
        return message("🏷", name, tags + (tags == 1 ? " tag" : " tags"));
    }

    private List<String> igLocations(InstagramProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<InstagramPost> posts = profile.posts();
        List<String> topLocations = AnalyticsUtils.topObjects(
                posts.stream()
                        .map(InstagramPost::location)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()),
                5);

        List<String> locations = topLocations.stream()
                .map(location -> {
                    List<InstagramPost> locationPosts = posts.stream()
                            .filter(post -> location.equals(post.location()))
                            .collect(Collectors.toList());

                    String from = locationPosts.stream()
                            .min(Comparator.comparing(InstagramPost::date))
                            .map(InstagramPost::date)
                            .map(date -> String.format("%02d", date.getMonthValue()) + "." + date.getYear())
                            .orElse(null);

                    String to = locationPosts.stream()
                            .max(Comparator.comparing(InstagramPost::date))
                            .map(InstagramPost::date)
                            .map(date -> String.format("%02d", date.getMonthValue()) + "." + date.getYear())
                            .orElse(null);

                    if (Objects.equals(from, to)) {
                        return " - " + location + " (" + locationPosts.size() + ": " + from + ")";
                    } else {
                        return " - " + location + " (" + locationPosts.size() + ": " + from + " - " + to + ")";
                    }
                })
                .collect(Collectors.toList());

        if (VKUtils.isNotEmpty(locations)) {
            TelegramUtils.log(IG, "[" + profile.identifier() + "] Downloaded locations");
            messages.add("\n📍 Locations:");
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

    private String like(String emoji, String name, Long number, Integer totalNumberOfItems) {
        return message(
                emoji + " " + name,
                (number + " / " + totalNumberOfItems) + (number == 1 ? " like" : " likes"));
    }
}
