package com.kiselev.enemy.network.telegram.api.bot.command.identifier;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils.*;

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

        String vkId = ProfilingUtils.identifier(SocialNetwork.VK, request);
        if (vkId != null) {
            String vkResponse = vk(vkId);
            publicEnemy.tg().send(requestId, TelegramMessage.raw(vkResponse));
            return;
        }

//        String vkId = ProfilingUtils.identifier(SocialNetwork.VK, request);
//        if (vkId != null) {
//
//            List<Profile> friends = publicEnemy.vk().service().api().friends(
//                    publicEnemy.vk().me().id()
//            );
//
//            for (Profile friend : friends) {
//                progress.bar(SocialNetwork.VK, "Friends", friends, friend);
//                vkId = friend.id();
//                String vkResponse = vk(vkId);
//                publicEnemy.tg().send(requestId, TelegramMessage.raw(vkResponse));
//            }
//            return;
//        }

//        TODO
//        String igId = ProfilingUtils.identifier(SocialNetwork.IG, request);
//        if (igId != null) {
//            EnemyMessage<InstagramProfile> igResponse = publicEnemy.ig().info(igId);
//            publicEnemy.tg().send(requestId, TelegramMessage.message(igResponse));
//            return;
//        }

        publicEnemy.tg().send(requestId, TelegramMessage.text(String.format("Request %s is not recognized", request)));
    }

    private String vk(String identifier) {
        List<String> messages = Lists.newArrayList();

        VKProfile profile = new VKProfile(publicEnemy.vk().service().api().profile(identifier));

        profile.friends(
                publicEnemy.vk().service().api().friends(profile.id()).stream()
                        .map(VKProfile::new)
                        .collect(Collectors.toList()));

        profile.area(
                profile.friends().stream()
                        .filter(VKProfile::isActive)
                        .peek(friend -> friend.friends(
                                publicEnemy.vk().service().api().friends(friend.id()).stream()
                                        .map(VKProfile::new)
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toMap(
                                friend -> friend,
                                VKProfile::friends)));

        InstagramProfile instagramProfile = ig(profile);

        List<InstagramPost> instagramPosts = posts(instagramProfile.id());

        // Information
        messages.addAll(information(profile));

        // Contacts
        messages.addAll(contacts(profile, instagramProfile));

        // Instagram
        messages.addAll(instagram(instagramProfile, instagramPosts));

        // Life
        messages.addAll(life(profile));

        // Likes
        messages.addAll(likes(profile, instagramPosts));

        // Hiders
        messages.addAll(hiders(profile));

        // Relatives
        messages.addAll(relatives(profile));

        // Predictions
        messages.addAll(predictions(profile));

        return messages.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }

    private InstagramProfile ig(VKProfile profile) {
        String instagram = profile.instagram();

        if (instagram != null) {
            return new InstagramProfile(publicEnemy.ig().service().api().profile(instagram));
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
                    return instagramProfile;
                }
            }
        }
        return new InstagramProfile();
    }

    private List<InstagramPost> posts(String id) {
        if (id != null) {
            return publicEnemy.ig().service().posts(id);
        }
        return Lists.newArrayList();
    }

    private List<String> information(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        messages.add(message("👤 Name", profile.fullName()));
//        messages.add(message("📝 Status", profile.status()));
        messages.add(message("🚻 Sex", profile.sex()));

        String age = profile.age();
        if (age != null) {
            messages.add(message("🔞 Age", age));
        } else {
            String hiddenAge = publicEnemy.vk().service().searchAge(profile);
            messages.add(message("🔞 Age", hiddenAge, "(hidden)"));
        }

        String birthDate = profile.birthDate();
        if (birthDate != null) {
            if (age != null) {
                messages.add(message("📅 Birth date", birthDate));
            } else {
                String hiddenBirthday = publicEnemy.vk().service().searchBirthDate(profile);
                if (hiddenBirthday != null) {
                    messages.add(message("📅 Birth date", birthDate, "(Hidden: " + hiddenBirthday + ")"));
                } else {
                    messages.add(message("📅 Birth date", birthDate));
                }
            }
        } else {
            String hiddenBirthday = publicEnemy.vk().service().searchBirthDate(profile);
            messages.add(message("📅 Birth date", hiddenBirthday, "(hidden)"));
        }

        if (birthDate != null) {
            List<Zodiac> zodiacs = VKUtils.zodiacs(birthDate);
            if (zodiacs.size() > 1) {
                Iterator<Zodiac> iterator = zodiacs.iterator();
                Zodiac zodiac1 = iterator.next();
                Zodiac zodiac2 = iterator.next();

                messages.add(message("🔄 Zodiac", "Cusp: " + zodiac1.title() + " - " + zodiac2.title()));
            } else {
                Zodiac zodiac = zodiacs.iterator().next();
                messages.add(message(zodiac.sign() + " Zodiac", zodiac.title()));
            }
        }

        messages.add(message("🏙 City", profile.city()));
        messages.add(message("🌎 Country", profile.country()));
        messages.add(message("🏠 Home town", profile.homeTown()));

        return messages;
    }

    private List<String> contacts(VKProfile profile, InstagramProfile instagramProfile) {
        List<String> messages = Lists.newArrayList();

        List<String> contacts = Lists.newArrayList();
        contacts.add(message("📞 Phone", ObjectUtils.firstNonNull(
                profile.phone(),
                instagramProfile.public_phone_number()
        )));
        contacts.add(message("📧 Email", instagramProfile.public_email()));
        contacts.add(link("📘 Facebook", "https://facebook.com/", profile.facebook()));
        contacts.add(link("✈️ Telegram", "https://t.me/", ObjectUtils.firstNonNull(
                profile.telegram(),
                instagramProfile.telegram()
        )));
        contacts.add(link("📷 Instagram", "https://www.instagram.com/", instagramProfile.username()));
        contacts.add(link("🐦 Twitter", "https://twitter.com/", profile.twitter()));
        contacts.add(message("📟 Skype", profile.skype()));

        if (VKUtils.isNotEmpty(contacts)) {
            messages.add("\nContacts:");
            messages.addAll(contacts);
        }

        return messages;
    }

    private List<String> instagram(InstagramProfile profile, List<InstagramPost> posts) {
        List<String> messages = Lists.newArrayList();

        List<String> instagram = Lists.newArrayList();
        instagram.add(message("🗣 Instagram type", profile.profileType()));

        List<String> locations = AnalyticsUtils.topObjects(
                posts.stream()
                        .map(InstagramPost::location)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), 5).stream()
                .map(location -> location(posts, location))
                .collect(Collectors.toList());

        instagram.add(IdentifierUtils.messages("📍 Locations", locations, 10));

        if (VKUtils.isNotEmpty(instagram)) {
            messages.add("\nInstagram:");
            messages.addAll(instagram);
        }

        return messages;
    }

    private String location(List<InstagramPost> posts, String location) {
        String from = posts.stream()
                .filter(post -> location.equals(post.location()))
                .min(Comparator.comparing(InstagramPost::date))
                .map(InstagramPost::date)
                .map(LocalDateTime::getYear)
                .map(String::valueOf)
                .orElse(null);

        String to = posts.stream()
                .filter(post -> location.equals(post.location()))
                .max(Comparator.comparing(InstagramPost::date))
                .map(InstagramPost::date)
                .map(LocalDateTime::getYear)
                .map(String::valueOf)
                .orElse(null);

        if (Objects.equals(from, to)) {
            return location + " (" + from + ")";
        } else {
            return location + " (" + from + " - " + to + ")";
        }
    }

    private List<String> life(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> life = Lists.newArrayList();
        life.add(messages("🏫 School", profile.school(), 8));
        life.add(messages("🏢 University", profile.university(), 11));
        life.add(messages("🏦 Job", profile.job(), 6));
        if (VKUtils.isNotEmpty(life)) {
            messages.add("\nLife:");
            messages.addAll(life);
        }

        return messages;
    }

    private List<String> likes(VKProfile profile, List<InstagramPost> instagramPosts) {
        List<String> messages = Lists.newArrayList();

        List<String> likes = Lists.newArrayList();

        likes.addAll(vkLikes(profile));
        likes.addAll(igLikes(instagramPosts));

        if (VKUtils.isNotEmpty(likes)) {
            messages.add("\nLikes for the last year:");
            messages.addAll(likes);
        }

        return messages;
    }

    private List<String> vkLikes(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> likes = Lists.newArrayList();
        List<VKProfile> vkLikes = publicEnemy.vk().service().likes(profile.id());

        Map<VKProfile, Long> vkLikersHeatMap = vkLikes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<VKProfile, Long>> vkLikers = vkLikersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<VKProfile, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        for (Map.Entry<VKProfile, Long> liker : vkLikers) {
            Long number = liker.getValue();
            likes.add(message("💙️ " + liker.getKey().fullName(), number + (number == 1 ? " like" : " likes")));
        }

        if (VKUtils.isNotEmpty(likes)) {
            messages.add("VK:");
            messages.addAll(likes);
        }

        return messages;
    }

    private List<String> igLikes(List<InstagramPost> instagramPosts) {
        List<String> messages = Lists.newArrayList();

        List<String> likes = Lists.newArrayList();
        List<Profile> igLikes = instagramPosts.stream()
                .filter(post -> post.date().isAfter(LocalDateTime.now().minusYears(1)))
                .map(post -> publicEnemy.ig().service().api().raw().likes(post.id()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Map<Profile, Long> igLikersHeatMap = igLikes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<Profile, Long>> igLikers = igLikersHeatMap.entrySet().stream()
                .sorted(Map.Entry.<Profile, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        for (Map.Entry<Profile, Long> liker : igLikers) {
            Long number = liker.getValue();
            likes.add(message("🧡 " + liker.getKey().username(), number + (number == 1 ? " like" : " likes")));
        }

        if (VKUtils.isNotEmpty(likes)) {
            messages.add("Instagram:");
            messages.addAll(likes);
        }

        return messages;
    }

    private List<String> hiders(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> hiders = Lists.newArrayList();
        Map<VKProfile, List<VKProfile>> area = profile.area();
        if (area != null) {
            for (Map.Entry<VKProfile, List<VKProfile>> hider : area.entrySet()) {
                if (!hider.getValue().contains(profile)) {
                    hiders.add(message("😶 Friend", hider.getKey().fullName()));
                }
            }
        }

        if (VKUtils.isNotEmpty(hiders)) {
            messages.add("\nHidden by friends:");
            messages.addAll(hiders);
        }

        return messages;
    }

    private List<String> relatives(VKProfile profile) {
        List<String> messages = Lists.newArrayList();

        List<String> relatives = Lists.newArrayList();

        List<VKProfile> people = publicEnemy.vk().service().relatives(profile.profile());
        for (VKProfile relative : people) {
            if (relative.isActive() && VKUtils.areFamilyMembers(profile.lastName(), relative.lastName())) { // TODO: Recheck
                relatives.add(message("🫂 Relative", relative.fullName()));
            }
        }

        List<VKProfile> friends = profile.friends();
        for (VKProfile friend : friends) {
            if (friend.isActive() && VKUtils.areFamilyMembers(profile.lastName(), friend.lastName())) { // TODO: Recheck
                relatives.add(message("🫂 Relative", friend.fullName()));
            }
        }

        if (VKUtils.isNotEmpty(relatives)) {
            messages.add("\nRelatives:");
            messages.addAll(relatives);
        }

        return messages;
    }

    private List<String> predictions(VKProfile profile) {
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
            messages.add("\nPredictions:");
            messages.addAll(predictions);
        }

        return messages;
    }
}
