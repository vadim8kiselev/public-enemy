package com.kiselev.enemy;

import com.kiselev.enemy.network.vk.api.internal.VKInternalAPI;
import com.kiselev.enemy.network.vk.api.model.Message;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.kiselev.enemy.service.profiler.PublicEnemyProfiler;
import com.kiselev.enemy.service.PublicEnemyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private VKInternalAPI api;

    public static void main(String[] args) {
        SpringApplication.run(PublicEnemyApplication.class, args);
    }

    @Override
    public void run(String... args) {


//        VKProfile kiselev = publicEnemyService.vk().profile("kiselev");
//        profiler.profile("vk.com/kiselev");

        if (1 < 2)
            return;

        Map<Profile, List<Message>> history = api.history(Collections.singletonList("174362175"));

        long today = LocalDate.now().toEpochDay();
        LocalDate epoch = LocalDate.ofEpochDay(0);

//        for (Map.Entry<Profile, List<Message>> entry : history.entrySet()) {
        Map.Entry<Profile, List<Message>> entry =  history.entrySet().iterator().next();
            Profile profile = entry.getKey();
            List<Message> messages = entry.getValue();

            Map<String, Long> map = messages.stream()
                    .map(Message::date)
                    .map(Long::valueOf)
                    .map(Instant::ofEpochSecond)
                    .map(instant -> LocalDateTime.ofInstant(instant, ZoneId.systemDefault()))
//                    .map(date -> ChronoUnit.DAYS.between(epoch, date))
                    .sorted(Comparator.naturalOrder())
                    .map(date -> date.format(DateTimeFormatter.ofPattern("M.yyyy")))
                    .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));

        Long max = map.values().stream()
                .max(Comparator.naturalOrder())
                .orElseThrow(RuntimeException::new);

        System.out.println(profile.firstName() + " " + profile.lastName());
        for (Map.Entry<String, Long> tap : map.entrySet()) {
            System.out.println(tap.getKey() + " - " + (tap.getValue() * 100 / max) + "%");
        }
        System.out.println(profile.firstName() + " " + profile.lastName());

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
