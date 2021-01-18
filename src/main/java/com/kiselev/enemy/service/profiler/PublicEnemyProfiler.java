package com.kiselev.enemy.service.profiler;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.merger.Merger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublicEnemyProfiler {

    private final PublicEnemyService publicEnemy;

    public Person profile(String url) {
        String vkId = ProfilingUtils.identifier(SocialNetwork.VK, url);
        if (vkId != null) {
            return vkProfile(vkId);
        }

        String igId = ProfilingUtils.identifier(SocialNetwork.IG, url);
        if (igId != null) {
            return igProfile(igId);
        }

        String tgId = ProfilingUtils.identifier(SocialNetwork.TG, url);
        if (tgId != null) {
            return tgProfile(tgId);
        }

        return null;
    }

    private Person vkProfile(String vkId) {
        Person person = person(SocialNetwork.VK, vkId);

//        List<Profile> vkFriends = publicEnemyService.vk().api().friends(vkProfile.id());
//
//        List<User> igFriends = vkFriends.stream()
//                .map(Profile::instagram)
//                .filter(Objects::nonNull)
//                .map(publicEnemyService.ig().api()::profile)
//                .collect(Collectors.toList());
//
//        Set<com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile> predictedProfiles = igFriends.stream()
//                .map(User::id)
//                .map(publicEnemyService.ig().api().raw()::friends)
//                .flatMap(List::stream)
//                .filter(profile -> vkProfile.fullName().equals(profile.fullName()))
//                .collect(Collectors.toSet());

        Person vkPerson = person(SocialNetwork.VK, person.getVk());
        Person igPerson = person(SocialNetwork.IG, person.getInstagram());
        Person tgPerson = person(SocialNetwork.TG, person.getTelegram());

        return Merger.merge(
                profile(SocialNetwork.VK, vkPerson),
                profile(SocialNetwork.IG, igPerson),
                profile(SocialNetwork.TG, tgPerson)
        );
    }

    private Person igProfile(String igId) {
        Person person = person(SocialNetwork.IG, igId);

        Person vkPerson = person(SocialNetwork.VK, person.getVk());
        Person igPerson = person(SocialNetwork.IG, person.getInstagram());
        Person tgPerson = person(SocialNetwork.TG, person.getTelegram());

        return Merger.merge(
                profile(SocialNetwork.VK, vkPerson),
                profile(SocialNetwork.IG, igPerson),
                profile(SocialNetwork.TG, tgPerson)
        );
    }

    private Person tgProfile(String tgId) {
        Person person = person(SocialNetwork.TG, tgId);

        Person vkPerson = person(SocialNetwork.VK, person.getVk());
        Person igPerson = person(SocialNetwork.IG, person.getInstagram());
        Person tgPerson = person(SocialNetwork.TG, person.getTelegram());

        return Merger.merge(
                profile(SocialNetwork.VK, vkPerson),
                profile(SocialNetwork.IG, igPerson),
                profile(SocialNetwork.TG, tgPerson)
        );
    }

    private Person profile(SocialNetwork socialNetwork, Person person) {
        // TODO: exclude existing person
        Person vkPerson = person(SocialNetwork.VK, person.getVk());
        Person igPerson = person(SocialNetwork.IG, person.getInstagram());
        Person tgPerson = person(SocialNetwork.TG, person.getTelegram());

        return Merger.merge(
                vkPerson,
                igPerson,
                tgPerson
        );
    }

    private Person person(SocialNetwork socialNetwork, String identifier) {
        switch (socialNetwork) {
            case VK: return Optional.ofNullable(publicEnemy.vk().profile(identifier)).map(Person::new).orElse(null);
            case IG: return Optional.ofNullable(publicEnemy.ig().profile(identifier)).map(Person::new).orElse(null);
            case TG: return Optional.ofNullable(publicEnemy.tg().profile(identifier)).map(Person::new).orElse(null);
            default:
                throw new RuntimeException("Unknown type of social network");
        }
    }
}
