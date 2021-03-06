package com.kiselev.enemy;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.vk.api.model.Message;
import com.kiselev.enemy.network.vk.model.VKProfile;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.model.Conversation;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.service.profiler.model.Text;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.analytics.AnalyticsUtils;
import com.kiselev.enemy.utils.analytics.model.Prediction;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kiselev.enemy.network.telegram.api.bot.command.identifier.utils.IdentifierUtils.messages;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.kiselev.enemy.data.mongo.repository")
public class PublicEnemyApplication implements CommandLineRunner {

    @Autowired
    private PublicEnemyService publicEnemy;

    public static void main(String[] args) {
        SpringApplication.run(PublicEnemyApplication.class, args);
    }

    @Override
    public void run(String... args) {

        InstagramProfile vadim8kiselev = publicEnemy.ig().profile("vadim8kiselev");
        for (InstagramProfile following : vadim8kiselev.following()) {
            List<Profile> followers = publicEnemy.ig().service().api().raw().followers(following.id());
            List<Profile> unique = followers.stream()
                    .distinct()
                    .collect(Collectors.toList());

//            publicEnemy.tg().send(TelegramMessage.raw(following.username() + ": " + unique.size() + " / " + followers.size() +
//                    " (" + Math.abs(unique.size() - followers.size()) + ")"));

            publicEnemy.tg().send(TelegramMessage.raw(following.username() + ";" + followers.size() + ";" + unique.size() + ";" + Math.abs(unique.size() - followers.size())));
        }
    }
//    @Override
//    public void run(String... args) {
//        List<String> ids = Lists.newArrayList(
//                "42597474",
//                "39218747",
//                "280512823",
//                "57565927",
//                "86763052",
//                "83412390"
//        );
//
//        List<List<VKProfile>> friends = ids.stream()
//                .map(id -> publicEnemy.vk().service().api()
//                        .friends(id).stream()
//                        .map(VKProfile::new)
//                        .collect(Collectors.toList()))
//                .collect(Collectors.toList());
//
//        Map<VKProfile, Long> heatMap = friends.stream()
//                .flatMap(List::stream)
//                .filter(friend -> !ids.contains(friend.id()))
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//
//        String message = heatMap.entrySet().stream()
//                .sorted(Map.Entry.<VKProfile, Long>comparingByValue().reversed())
//                .limit(30)
//                .map(entry -> entry.getKey().name() + " - " + entry.getValue() + " / " + friends.size())
//                .collect(Collectors.joining("\n"));
//
//        publicEnemy.tg().send(TelegramMessage.text("Closest friends\n" + message));
//        System.exit(0);
//    }

    private void sortedMessages() {
        VKProfile vk_me = publicEnemy.vk().me();

        Map<VKProfile, Set<Message>> vkRawHistory = publicEnemy.vk().service().history();
        List<Conversation> vkHistory = vkRawHistory.entrySet().stream()
                .map(entry -> Conversation.builder()
                        .id(entry.getKey().id())
                        .person(new Person(entry.getKey()))
                        .texts(entry.getValue().stream()
                                .map(Text::new)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        List<String> dialogues = vkHistory.stream()
                .filter(conversation -> conversation.getTexts() != null)
                .sorted((a, b) -> {
                    int a_size = a.getTexts() != null ? a.getTexts().size() : 0;
                    int b_size = b.getTexts() != null ? b.getTexts().size() : 0;
                    return Integer.compare(b_size, a_size);
                })
                .map(conversation -> conversation.getPerson().getFullName() + " - " + conversation.getTexts().size() + " messages")
                .limit(30)
                .collect(Collectors.toList());

        for (String dialogue : dialogues) {
            publicEnemy.tg().send(TelegramMessage.text(dialogue));
        }
    }

    private void cities() {
        VKProfile kiselev = publicEnemy.vk().profile("kiselev");

        Set<VKProfile> friends = publicEnemy.vk().service().api().friends(kiselev.id())
                .stream()
                .map(VKProfile::new)
                .map(friend -> publicEnemy.vk().service().api().friends(friend.id()).stream()
                        .map(VKProfile::new).collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (VKProfile friend : friends) {
            String city = friend.city();
            if (city != null) {
                List<VKProfile> ffriends = publicEnemy.vk().service().api().friends(friend.id()).stream()
                        .map(VKProfile::new).collect(Collectors.toList());

                Prediction<String> prediction = AnalyticsUtils.predict(VKProfile::city, ffriends);
                if (prediction != null && prediction.sufficient(20)) {
                    String predictedCity = prediction.value();

                    if (ObjectUtils.notEqual(city, predictedCity)) {
                        publicEnemy.tg().send(TelegramMessage.text(friend.name() + " - City:" + city + ", Predicted City: " + prediction.message()));
                    }
                }
            }
        }
    }

    public void birthdates() {
        VKProfile me = publicEnemy.vk().me();
        List<VKProfile> friends = publicEnemy.vk().service().api().friends(me.id()).stream()
                .map(VKProfile::new)
                .sorted(Comparator.comparing(VKProfile::lastName))
                .collect(Collectors.toList());

        int number = 0;
        for (VKProfile friend : friends) {
            String birthDate = friend.birthDate();
            if (birthDate == null) {
                birthDate = publicEnemy.vk().service().searchBirthDate(friend);
            }

            String age = friend.age();
            if (age == null) {
                age = publicEnemy.vk().service().searchAge(friend);
            }

            if (birthDate == null || age == null) {
                boolean debug = true;
            }
            publicEnemy.tg().send(TelegramMessage.raw(number++ + ": " + friend.fullName() + " - " + birthDate + "(" + age + ")"));
        }
    }

    @SneakyThrows
    public void stats() {
        VKProfile vadim8kiselev = new VKProfile(publicEnemy.vk().service().api().profile("asya_ov"));

        List<VKProfile> friends = publicEnemy.vk().service().api().friends(vadim8kiselev.id()).stream()
                .map(VKProfile::new)
                .collect(Collectors.toList());

        List<VKProfile> a = friends.stream()
                .filter(friend -> "25.9".equals(friend.birthDate()))
                .collect(Collectors.toList());

        List<VKProfile> b = friends.stream()
                .filter(friend -> StringUtils.isNotEmpty(friend.telegram()))
                .collect(Collectors.toList());

        Field[] fields = vadim8kiselev.getClass().getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
//            Object object = ReflectionUtils.getField(field, vadim8kiselev);

            List<Object> predictionCandidates = friends.stream()
                    .map(friend -> ReflectionUtils.getField(field, friend))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Map<Object, Long> predictionCandidatesHeatMap = predictionCandidates.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Map<Object, Long> sortedPredictionCandidatesHeatMap = predictionCandidatesHeatMap.entrySet().stream()
                    .sorted(Map.Entry.<Object, Long>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (u, v) -> v,
                            LinkedHashMap::new));

            String name = field.getName();
            String message = sortedPredictionCandidatesHeatMap.entrySet().stream()
                    .map(entry -> name + ": " + entry.getKey().toString() + ", number: " + entry.getValue())
                    .collect(Collectors.joining("\n"));

            try {
                publicEnemy.tg().send(TelegramMessage.text(message));
            } catch (Exception exception) {
                // skip
            }
        }
    }

    //    @SneakyThrows
//    public void colors() {
//        InstagramProfile vadim8kiselev = service.ig().profile("vadim8kiselev");
//        List<InstagramPost> posts = vadim8kiselev.posts();
//
//        List<Media> photos = posts.stream()
//                .map(InstagramPost::photo)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        Map<Color, Long> colors = Maps.newHashMap();
//
//        for (Media photo : photos) {
//            List<Color> mediaColors = photo.colors();
//
//            Map<Color, Long> photoColors = mediaColors.stream()
//                    .collect(Collectors.groupingBy(
//                            Function.identity(),
//                            Collectors.counting()));
//
//            for (Map.Entry<Color, Long> entry : photoColors.entrySet()) {
//                Color key = entry.getKey();
//                Long value = entry.getValue();
//
//                if (colors.get(key) != null) {
//                    Long count = colors.get(key);
//                    colors.put(key, count + value);
//                } else {
//                    colors.put(key, value);
//                }
//            }
//        }
//
//        String colorsResult = colors.entrySet().stream()
//                .sorted(Map.Entry.<Color, Long>comparingByValue().reversed())
//                .map(entry -> entry.getKey().getName() + " - " + entry.getValue())
//                .collect(Collectors.joining("\n"));
//
//        service.tg().send(TelegramMessage.message(colorsResult));
//    }
//
    @SneakyThrows
    public void messages() {

//        InstagramProfile ig_me = instagram.me();
//        TelegramProfile tg_me = telegram.me();
//        VKProfile vk_me = vk.me();

//        Map<InstagramProfile, List<ThreadItem>> instagramRawHistory = instagram.service().history();
//        List<Conversation> instagramHistory = instagramRawHistory.entrySet().stream()
//                .map(entry -> Conversation.builder()
//                        .id(entry.getKey().id())
//                        .person(new Person(entry.getKey()))
//                        .texts(entry.getValue().stream()
//                                .map(Text::new)
//                                .collect(Collectors.toList()))
//                        .build())
//                .collect(Collectors.toList());
//        ProfilingUtils.cache("ig", instagramHistory);
        List<Conversation> instagramConversations = ProfilingUtils.cache("ig_mine");

//        List<Conversation> igUpdatedConversations = instagramConversations.stream()
//                .peek(conversation -> {
//                    List<Text> texts = conversation.getTexts();
//                    texts.forEach(text -> text.setMine(Objects.equals(ig_me.id(), text.getFrom())));
//                })
//                .collect(Collectors.toList());
//        ProfilingUtils.cache("ig_mine", igUpdatedConversations);

//        Map<TelegramUser, List<TLMessage>> telegramRawHistory = telegram.history();
//        List<Conversation> telegramHistory = telegramRawHistory.entrySet().stream()
//                .map(entry -> Conversation.builder()
//                        .id(String.valueOf(entry.getKey().id()))
//                        .person(new Person(entry.getKey()))
//                        .texts(entry.getValue().stream()
//                                .map(Text::new)
//                                .collect(Collectors.toList()))
//                        .build())
//                .collect(Collectors.toList());
//        ProfilingUtils.cache("tg", telegramHistory);
        List<Conversation> telegramConversations = ProfilingUtils.cache("tg_mine");

//        List<Conversation> tgUpdatedConversations = telegramConversations.stream()
//                .peek(conversation -> {
//                    List<Text> texts = conversation.getTexts();
//                    texts.forEach(text -> text.setMine(Objects.equals(tg_me.id(), text.getFrom())));
//                })
//                .collect(Collectors.toList());
//        ProfilingUtils.cache("tg_mine", tgUpdatedConversations);

//        List<Conversation> updatedTg = telegramConversations.stream()
//                .map(conversation -> {
//                    Person person = conversation.getPerson();
//                    String username = StringUtils.isNotEmpty(person.getTelegram()) ? person.getTelegram() : null;
//                    String phone = StringUtils.isNotEmpty(person.getPhone()) ? person.getPhone() : null;
//
//                    person.setTelegram(ObjectUtils.firstNonNull(username, phone));
//                    person.setPhone(phone);
//
//                    return conversation;
//                }).collect(Collectors.toList());
//
//        ProfilingUtils.cache("tg_xx", updatedTg);

//        Map<VKProfile, List<Message>> vkRawHistory = vk.service().history();
//        List<Conversation> vkHistory = vkRawHistory.entrySet().stream()
//                .map(entry -> Conversation.builder()
//                        .id(entry.getKey().id())
//                        .person(new Person(entry.getKey()))
//                        .texts(entry.getValue().stream()
//                                .map(Text::new)
//                                .collect(Collectors.toList()))
//                        .build())
//                .collect(Collectors.toList());
//        ProfilingUtils.cache("vk", vkHistory);
        List<Conversation> vkConversations = ProfilingUtils.cache("vk_mine");

//        List<Conversation> vkUpdatedConversations = vkConversations.stream()
//                .peek(conversation -> {
//                    List<Text> texts = conversation.getTexts();
//                    texts.forEach(text -> text.setMine(Objects.equals(vk_me.id(), text.getFrom())));
//                })
//                .collect(Collectors.toList());
//        ProfilingUtils.cache("vk_mine", vkUpdatedConversations);

        List<Conversation> conversations = Lists.newArrayList();
        conversations.addAll(instagramConversations);
        conversations.addAll(telegramConversations);
        conversations.addAll(vkConversations);

        conversations
                .forEach(conversation -> conversation.setTexts(
                        conversation.getTexts().stream().distinct().collect(Collectors.toList())
                ));

//        Map<Person, List<Text>> history = conversations.stream()
//                .collect(Collectors.toMap(
//                        Conversation::getPerson,
//                        Conversation::getTexts
//                ));
//
//        List<String> instagrams = instagramConversations.stream()
//                .map(Conversation::getPerson)
//                .map(Person::getInstagram)
//                .collect(Collectors.toList());
//
//        List<String> telegrams = telegramConversations.stream()
//                .map(Conversation::getPerson)
//                .map(Person::getTelegram)
//                .collect(Collectors.toList());
//
//        List<String> vks = vkConversations.stream()
//                .map(Conversation::getPerson)
//                .map(Person::getVk)
//                .collect(Collectors.toList());
//
//        Sets.SetView<String> ig_tg = Sets.intersection(
//                Sets.newHashSet(instagrams),
//                Sets.newHashSet(telegrams)
//        );
//
//        Sets.SetView<String> ig_vk = Sets.intersection(
//                Sets.newHashSet(instagrams),
//                Sets.newHashSet(vks)
//        );
//
//        Sets.SetView<String> tg_vk = Sets.intersection(
//                Sets.newHashSet(telegrams),
//                Sets.newHashSet(vks)
//        );
//
//        Sets.SetView<String> ig_tg_vk = Sets.intersection(
//                Sets.newHashSet(ig_tg),
//                Sets.newHashSet(tg_vk)
//        );    й

        Set<String> ids = conversations.stream()
                .map(Conversation::getTexts)
                .flatMap(List::stream)
                .filter(Text::getMine)
                .map(Text::getFrom)
                .collect(Collectors.toSet());

        Map<String, Long> texts = conversations.stream()
                .map(Conversation::getTexts)
                .flatMap(List::stream)
                .filter(Text::getMine)
                .map(Text::getText)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .map(string -> string
                        .replaceAll("[?!:;.,()\\d*<>]", ""))
                .map(string -> string
                        .replaceAll("\\s+", " "))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        LinkedHashMap<String, Long> desc = texts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        LinkedHashMap<String, Long> asc = texts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        LinkedHashMap<String, Long> words1 = desc.entrySet().stream()
                .filter(entry -> entry.getKey().split(" ").length == 1)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        LinkedHashMap<String, Long> words2 = desc.entrySet().stream()
                .filter(entry -> entry.getKey().split(" ").length == 2)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        LinkedHashMap<String, Long> words3 = desc.entrySet().stream()
                .filter(entry -> entry.getKey().split(" ").length == 3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        LinkedHashMap<String, Long> words4 = desc.entrySet().stream()
                .filter(entry -> entry.getKey().split(" ").length == 4)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        LinkedHashMap<String, Long> words5 = desc.entrySet().stream()
                .filter(entry -> entry.getKey().split(" ").length == 5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> v,
                        LinkedHashMap::new));

        boolean debug = true;

//        List<Conversation> history = Merger.reduce(conversations);
//
//        Conversation conversation = history.stream()
//                .filter(c -> c.getPerson().getFullName().equals("Anastasia Ovcharova"))
//                .findFirst()
//                .orElse(null);
//
////        for (Conversation conversation : history) {
//
//        Person person = conversation.getPerson();
//        List<Text> texts = conversation.getTexts();
//
//        int me = 0;
//        int somebody = 0;
//
//        for (int index = 1; index < texts.size(); index++) {
//            Text firstMessage = texts.get(index - 1);
//            Text secondMessage = texts.get(index);
//
//            LocalDateTime firstMessageDate = firstMessage.getDate();
//            LocalDateTime secondMessageDate = secondMessage.getDate();
//
//            if (firstMessageDate.until(secondMessageDate, ChronoUnit.DAYS) > 1) {
//                if (secondMessage.getMine()) {
//                    me++;
//                } else {
//                    somebody++;
//                }
//            }
//        }
//
//        publicEnemy.tg().send(TelegramMessage.message(
//                String.format(
//                        "\\[%s\\] wrote me %s times. I wrote back %s times.",
//                        ObjectUtils.firstNonNull(
//                                person.getFullName(),
//                                person.getInstagram(),
//                                person.getVk(),
//                                person.getTelegram()),
//                        somebody,
//                        me
//                )
//        ));
////        }
//
//        boolean debug = true;


//        long today = LocalDate.now().toEpochDay();
//        LocalDate epoch = LocalDate.ofEpochDay(0);

//        for (Map.Entry<Profile, List<Message>> entry : history.entrySet()) {
//        Conversation conversation = history.iterator().next();

//        Person profile = alina_borozdina.getPerson();
//        List<Text> messages = alina_borozdina.getTexts();
//
//        Map<String, Long> map = messages.stream()
//                .map(Text::getDate)
////                    .map(date -> ChronoUnit.DAYS.between(epoch, date))
//                .sorted(Comparator.naturalOrder())
//                .map(date -> date.format(DateTimeFormatter.ofPattern("M.yyyy")))
//                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
//
//        Long max = map.values().stream()
//                .max(Comparator.naturalOrder())
//                .orElseThrow(RuntimeException::new);
//
//        System.out.println(profile.getFirstName() + " " + profile.getLastName());
//        for (Map.Entry<String, Long> tap : map.entrySet()) {
//            System.out.println(tap.getKey() + " - " + (tap.getValue() * 100 / max) + "%");
//        }
//        System.out.println(profile.getFirstName() + " " + profile.getLastName());

//
//            List<Long> diffs = Lists.newArrayList();
//            for (int index = 1; index < days.size(); index++) {
//                Long a = days.get(index - 1);
//                Long b = days.get(index);
//
//                long c = a - b;
//                diffs.add(c);
//            }
//
//            Long max = diffs.stream()
//                    .max(Comparator.naturalOrder())
//                    .orElse(null);
//
//            System.out.println(profile.firstName() + " " + profile.lastName() + " - " + max + " days without talking at max");
//        }
    }

    private List<Conversation> lookFor(List<Conversation> conversations, String message) {
        List<Conversation> convs = Lists.newArrayList();

        for (Conversation conversation : conversations) {
            Person person = conversation.getPerson();
            List<Text> texts = conversation.getTexts();

            for (Text text : texts) {
                if (text.getMine() && text.getText() != null) {
                    String textMessage = String
                            .valueOf(text.getText())
                            .toLowerCase()
                            .replaceAll("[?!:;.,()\\d*<>]", "")
                            .replaceAll("\\s+", " ")
                            .trim();

                    if (textMessage.equals(message)) {
                        convs.add(Conversation.builder()
                                .person(person)
                                .texts(Lists.newArrayList(
                                        text
                                ))
                                .id(conversation.getId())
                                .build());
                    }
                }
            }
        }
        return convs;
    }
}
