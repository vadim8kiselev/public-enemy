package com.kiselev.enemy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kiselev.enemy.network.instagram.Instagram;
import com.kiselev.enemy.network.instagram.model.InstagramPost;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.Telegram;
import com.kiselev.enemy.network.telegram.model.TelegramMessage;
import com.kiselev.enemy.network.vk.VK;
import com.kiselev.enemy.service.PublicEnemyService;
import com.kiselev.enemy.service.profiler.PublicEnemyProfiler;
import com.kiselev.enemy.service.profiler.model.Conversation;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.service.profiler.model.Text;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.image.Color;
import com.kiselev.enemy.utils.image.Media;
import com.kiselev.enemy.utils.merger.Merger;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.kiselev.enemy.data.mongo.repository")
public class PublicEnemyApplication implements CommandLineRunner {

    @Autowired
    private PublicEnemyService publicEnemyService;

    @Autowired
    private PublicEnemyProfiler profiler;

    @Autowired
    private Instagram instagram;

    @Autowired
    private VK vk;

    @Autowired
    private Telegram telegram;

    public static void main(String[] args) {
        SpringApplication.run(PublicEnemyApplication.class, args);
    }

    @Override
    @SneakyThrows
    public void run(String... args) {

    }

    @SneakyThrows
    public void colors() {
        InstagramProfile vadim8kiselev = instagram.profile("vadim8kiselev");
        List<InstagramPost> posts = vadim8kiselev.posts();

        List<Media> photos = posts.stream()
                .map(InstagramPost::photo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<Color, Long> colors = Maps.newHashMap();

        for (Media photo : photos) {
            List<Color> mediaColors = photo.colors();

            Map<Color, Long> photoColors = mediaColors.stream()
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            Collectors.counting()));

            for (Map.Entry<Color, Long> entry : photoColors.entrySet()) {
                Color key = entry.getKey();
                Long value = entry.getValue();

                if (colors.get(key) != null) {
                    Long count = colors.get(key);
                    colors.put(key, count + value);
                } else {
                    colors.put(key, value);
                }
            }
        }

        String colorsResult = colors.entrySet().stream()
                .sorted(Map.Entry.<Color, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey().getName() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));

        telegram.send(TelegramMessage.message(colorsResult));
    }

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
//        );

        List<Conversation> history = Merger.reduce(conversations);

        Conversation conversation = history.stream()
                .filter(c -> c.getPerson().getFullName().equals("Anastasia Ovcharova"))
                .findFirst()
                .orElse(null);

//        for (Conversation conversation : history) {

        Person person = conversation.getPerson();
        List<Text> texts = conversation.getTexts();

        int me = 0;
        int somebody = 0;

        for (int index = 1; index < texts.size(); index++) {
            Text firstMessage = texts.get(index - 1);
            Text secondMessage = texts.get(index);

            LocalDateTime firstMessageDate = firstMessage.getDate();
            LocalDateTime secondMessageDate = secondMessage.getDate();

            if (firstMessageDate.until(secondMessageDate, ChronoUnit.DAYS) > 1) {
                if (secondMessage.getMine()) {
                    me++;
                } else {
                    somebody++;
                }
            }
        }

        telegram.send(TelegramMessage.message(
                String.format(
                        "\\[%s\\] wrote me %s times. I wrote back %s times.",
                        ObjectUtils.firstNonNull(
                                person.getFullName(),
                                person.getInstagram(),
                                person.getVk(),
                                person.getTelegram()),
                        somebody,
                        me
                )
        ));
//        }

        boolean debug = true;


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
}
