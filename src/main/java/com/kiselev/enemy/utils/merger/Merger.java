package com.kiselev.enemy.utils.merger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kiselev.enemy.service.profiler.model.Conversation;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.service.profiler.model.Text;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Merger {

    public static List<Conversation> reduce(List<Conversation> conversations) {
        Map<String, Set<Conversation>> history = Maps.newHashMap();

        for (Conversation conversation1 : conversations) {
            for (Conversation conversation2 : conversations) {
                if (ObjectUtils.notEqual(conversation1, conversation2)
                        && rule(conversation1, conversation2)) {
                    String identifier1 = ObjectUtils.firstNonNull(
                            conversation1.getPerson().getInstagram(),
                            conversation1.getPerson().getTelegram(),
                            conversation1.getPerson().getVk());

                    if (history.get(identifier1) == null) {
                        history.put(identifier1, Sets.newHashSet(conversation2));
                    } else {
                        history.get(identifier1).add(conversation2);
                    }
                }
            }
        }

        return history.values().stream()
                .map(Lists::newArrayList)
                .filter(list -> list.size() > 1)
                .map(group -> {
                    return group.stream()
                            .reduce(Merger::merge)
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static boolean rule(Conversation a, Conversation b) {
        Person a_person = a.getPerson();
        Person b_person = b.getPerson();

        Set<String> a_identifier = Stream.of(
                a_person.getInstagram(),
                a_person.getTelegram(),
                a_person.getVk()
        )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> b_identifier = Stream.of(
                b_person.getInstagram(),
                b_person.getTelegram(),
                b_person.getVk()
        )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        boolean identifier = CollectionUtils.isNotEmpty(Sets.intersection(a_identifier, b_identifier));

        String a_fullName = a_person.getFullName();
        String b_fullName = b_person.getFullName();

        boolean fullName = StringUtils.isNotEmpty(a_fullName)
                && Objects.equals(a_fullName, b_fullName);

        String a_phoneNumber = a_person.getPhone();
        String b_phoneNumber = b_person.getPhone();

        boolean phoneNumber = StringUtils.isNotEmpty(a_phoneNumber)
                && Objects.equals(a_phoneNumber, b_phoneNumber);

        return identifier
                || fullName // TODO: Might cause problems
                || phoneNumber;
    }

    public static Conversation merge(Conversation a, Conversation b) {
        String a_id = a.getId();
        Person a_person = a.getPerson();
        List<Text> a_texts = a.getTexts();

        String b_id = b.getId();
        Person b_person = b.getPerson();
        List<Text> b_texts = b.getTexts();

        String id = a_id + "_" + b_id;

        Person person = merge(a_person, b_person);

        Set<Text> texts = Sets.newHashSet();
        texts.addAll(a_texts);
        texts.addAll(b_texts);

        return Conversation.builder()
                .id(id)
                .person(person)
                .texts(texts.stream()
                        .sorted(Comparator.comparing(Text::getDate))
                        .collect(Collectors.toList()))
                .build();
    }

    public static Person merge(Person a, Person b) {
        return Person.builder()
                .firstName(ObjectUtils.firstNonNull(a.getFirstName(), b.getFirstName()))
                .lastName(ObjectUtils.firstNonNull(a.getLastName(), b.getLastName()))
                .fullName(ObjectUtils.firstNonNull(a.getFullName(), b.getFullName()))
                .sex(ObjectUtils.firstNonNull(a.getSex(), b.getSex()))
                .age(ObjectUtils.firstNonNull(a.getAge(), b.getAge()))
                .birthday(ObjectUtils.firstNonNull(a.getBirthday(), b.getBirthday()))
                .phone(ObjectUtils.firstNonNull(a.getPhone(), b.getPhone()))
                .email(ObjectUtils.firstNonNull(a.getEmail(), b.getEmail()))
                .country(ObjectUtils.firstNonNull(a.getCountry(), b.getCountry()))
                .city(ObjectUtils.firstNonNull(a.getCity(), b.getCity()))
                .address(ObjectUtils.firstNonNull(a.getAddress(), b.getAddress()))
                .telegram(ObjectUtils.firstNonNull(a.getTelegram(), b.getTelegram()))
                .instagram(ObjectUtils.firstNonNull(a.getInstagram(), b.getInstagram()))
                .vk(ObjectUtils.firstNonNull(a.getVk(), b.getVk()))
                .build();
    }
}
