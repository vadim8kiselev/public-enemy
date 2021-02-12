package com.kiselev.enemy.network.telegram.api.bot.command.Identifier;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.api.bot.command.TelegramCommand;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.network.vk.utils.VKUtils;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            publicEnemy.tg().send(requestId, TelegramMessage.text(vkResponse));
            return;
        }

        String igId = ProfilingUtils.identifier(SocialNetwork.IG, request);
        if (igId != null) {
            EnemyMessage<InstagramProfile> igResponse = publicEnemy.ig().info(igId);
            publicEnemy.tg().send(requestId, TelegramMessage.message(igResponse));
            return;
        }

        publicEnemy.tg().send(requestId, TelegramMessage.text(String.format("Request %s is not recognized", request)));
    }

//    private <Type extends Info> EnemyMessage<Type> info(SocialNetwork network, String identifier) {
//        switch (network) {
//            case VK: return publicEnemy.vk().info(identifier);
////            case IG: return publicEnemy.ig().info(identifier);
////            case TG: return publicEnemy.tg().info(identifier);
//            default: throw new RuntimeException("Unknown type of social network!");
//        }
//    }

    private String vk(String identifier) {
        List<String> messages = Lists.newArrayList();

        VKProfile profile = new VKProfile(publicEnemy.vk().service().api().profile(identifier));
        InstagramProfile instagramProfile = ig(profile);

        List<VKProfile> friends = publicEnemy.vk().service().api().friends(profile.id()).stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());


//        messages.add(message("Username", profile.username()));
        messages.add(message("Full name", profile.fullName()));
        messages.add(message("Status", profile.status()));
        messages.add(message("Sex", profile.sex()));

        String age = profile.age();
        if (age != null) {
            messages.add(message("Age", age));
        } else {
            String hiddenAge = publicEnemy.vk().service().searchAge(profile);
            messages.add(message("Age", hiddenAge, "\\(hidden\\)"));
        }

        String birthDate = profile.birthDate();
        if (birthDate != null) {
            if (age != null) {
                messages.add(message("Birth date", birthDate));
            } else {
                String hiddenBirthday = publicEnemy.vk().service().searchBirthDate(profile);
                if (hiddenBirthday != null) {
                    messages.add(message("Birth date", birthDate, "\\(Hidden: " + hiddenBirthday + "\\)"));
                } else {
                    messages.add(message("Birth date", birthDate));
                }
            }
        } else {
            String hiddenBirthday = publicEnemy.vk().service().searchBirthDate(profile);
            messages.add(message("Birth date", hiddenBirthday, "\\(hidden\\)"));
        }

        messages.add(message("Country", profile.country()));
        messages.add(message("City", profile.city()));
        messages.add(message("Home town", profile.homeTown()));


        List<String> contacts = Lists.newArrayList();
        contacts.add(message("Phone", ObjectUtils.firstNonNull(
                profile.phone(),
                instagramProfile.public_phone_number()
        )));
        contacts.add(message("Email", instagramProfile.public_email()));
        contacts.add(message("Facebook", profile.facebook()));
        contacts.add(message("Instagram", profile.instagram()));
        contacts.add(message("Instagram type", instagramProfile.profileType()));
        contacts.add(message("Twitter", profile.twitter()));
        contacts.add(message("Telegram", ObjectUtils.firstNonNull(
                profile.telegram(),
                instagramProfile.telegram()
        )));
        contacts.add(message("Skype", profile.skype()));
        if (VKUtils.isNotEmpty(contacts)) {
            messages.add("\nContacts:");
            messages.addAll(contacts);
        }

        List<String> life = Lists.newArrayList();
        life.add(messages("School", profile.school()));
        life.add(messages("University", profile.university()));
        life.add(messages("Job", profile.job()));
        if (VKUtils.isNotEmpty(life)) {
            messages.add("\nLife:");
            messages.addAll(life);
        }

//        for (VKProfile friend : friends) {
//            List<VKProfile> ffriends = publicEnemy.vk().service().api().friends(friend.id()).stream()
//                    .map(VKProfile::new)
//                    .collect(Collectors.toList());
//
//            if (CollectionUtils.isNotEmpty(ffriends)) {
//                if (friend.age() == null) {
//                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::age, ffriends);
//                    if (prediction != null) {
//                        friend.age(prediction.value());
//                    }
//                }
//                if (friend.country() == null) {
//                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::country, ffriends);
//                    if (prediction != null) {
//                        friend.country(prediction.value());
//                    }
//                }
//                if (friend.city() == null) {
//                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::city, ffriends);
//                    if (prediction != null) {
//                        friend.city(prediction.value());
//                    }
//                }
//                if (friend.homeTown() == null) {
//                    Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::homeTown, ffriends);
//                    if (prediction != null) {
//                        friend.homeTown(prediction.value());
//                    }
//                }
//                if (friend.school() == null) {
//                    List<String> schools = ffriends.stream()
//                            .map(VKProfile::school)
//                            .flatMap(List::stream)
//                            .filter(Objects::nonNull)
//                            .collect(Collectors.toList());
//                    Prediction<String> prediction = AnalyticsUtils.predict(schools);
//                    if (prediction != null) {
//                        friend.school(Collections.singletonList(
//                                prediction.value()
//                        ));
//                    }
//                }
//                if (friend.university() == null) {
//                    List<String> universities = ffriends.stream()
//                            .map(VKProfile::university)
//                            .flatMap(List::stream)
//                            .filter(Objects::nonNull)
//                            .collect(Collectors.toList());
//                    Prediction<String> prediction = AnalyticsUtils.predict(universities);
//                    if (prediction != null) {
//                        friend.university(Collections.singletonList(
//                                prediction.value()
//                        ));
//                    }
//                }
//                if (friend.job() == null) {
//                    List<String> jobs = ffriends.stream()
//                            .map(VKProfile::job)
//                            .flatMap(List::stream)
//                            .filter(Objects::nonNull)
//                            .collect(Collectors.toList());
//                    Prediction<String> prediction = AnalyticsUtils.predict(jobs);
//                    if (prediction != null) {
//                        friend.job(Collections.singletonList(
//                                prediction.value()
//                        ));
//                    }
//                }
//            }
//            progress.bar(SocialNetwork.VK, "Friends of friends", friends, friend);
//        }

        List<String> predictions = Lists.newArrayList();
        predictions.add(prediction("Age", VKProfile::age, friends));
        predictions.add(prediction("Country", VKProfile::country, friends));
        predictions.add(prediction("City", VKProfile::city, friends));
        predictions.add(prediction("Home town", VKProfile::homeTown, friends));
        predictions.add(predictions("School", VKProfile::school, friends));
        predictions.add(predictions("University", VKProfile::university, friends));
        predictions.add(predictions("Job", VKProfile::job, friends));
        if (VKUtils.isNotEmpty(predictions)) {
            messages.add("\nPredictions:");
            messages.addAll(predictions);
        }

        return messages.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }

    private InstagramProfile ig(VKProfile profile) {
        String instagram = profile.instagram();
        if (instagram != null) {
            return new InstagramProfile(publicEnemy.ig().service().api().profile(instagram));
        }
        return new InstagramProfile();
    }

    private <A, B> String prediction(String title,
                                     Function<A, B> function,
                                     List<A> predictionGroup) {
        Prediction<B> prediction = AnalyticsUtils.predict(function, predictionGroup);
        if (prediction != null && prediction.sufficient(10)) {
            return message(title, prediction.message());
        }
        return null;
    }

    private <A, B> String predictions(String title,
                                      Function<A, List<B>> function,
                                      List<A> predictionGroup) {
        List<B> predictionCandidates = predictionGroup.stream()
                .map(function)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Prediction<B> prediction = AnalyticsUtils.predict(predictionCandidates);
        if (prediction != null && prediction.sufficient(20)) {
            return message(title, prediction.message());
        }
        return null;
    }

    private String message(String title, Object field) {
        if (field != null) {
            String string = field.toString();
            if (StringUtils.isNotEmpty(string)) {
                return title + ": " + field.toString();
            }
        }
        return null;
    }

    private String message(String title, Object field, String postfix) {
        if (field != null) {
            String string = field.toString();
            if (StringUtils.isNotEmpty(string)) {
                return title + ": " + field.toString() + " " + postfix;
            }
        }
        return null;
    }

    private String messages(String title, List<String> fields) {
        if (CollectionUtils.isNotEmpty(fields)) {
            String indent = StringUtils.repeat(" ", title.length());

            List<String> page = Lists.newArrayList();
            for (int index = 0; index < fields.size(); index++) {
                String field = fields.get(index);
                if (index == 0) {
                    page.add(title + ": " + field);
                } else {
                    page.add(indent + "  " + field);
                }
            }

            return String.join("\n", page);
        }
        return null;
    }
}
