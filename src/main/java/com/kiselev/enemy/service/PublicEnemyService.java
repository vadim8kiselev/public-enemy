package com.kiselev.enemy.service;

import com.kiselev.enemy.network.instagram.Instagram;
import com.kiselev.enemy.network.vk.VK;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicEnemyService {

    private final Instagram ig;

    private final VK vk;

    public Instagram ig() {
        return ig;
    }

    public VK vk() {
        return vk;
    }

//    public void run() {
//        InstagramProfile vadim8kiselev = instagram.profile("vadim8kiselev");
//        List<InstagramProfile> followers = vadim8kiselev.followers();
//        List<InstagramProfile> following = vadim8kiselev.following();
//        List<InstagramPost> posts = vadim8kiselev.posts();
//        for (InstagramPost post : posts) {
//            //List<InstagramProfile> likes = post.likes();
//            //List<InstagramCommentary> commentaries = post.commentaries();
//
//            boolean debug = true;
//        }
//
//        int sum = posts.stream()
//                .mapToInt(InstagramPost::likeCount)
//                .sum();
//
//        following.removeAll(followers);
//
//        List<String> names = following.stream()
//                .map(InstagramProfile::name)
//                .collect(Collectors.toList());
//
//        boolean debug = true;
//
//
////
////        List<InstagramProfile> likes = posts.stream()
////                .map(InstagramPost::likes)
////                .flatMap(List::stream)
////                .collect(Collectors.toList());
////
////        Map<String, Long> map = likes.stream()
////                .map(InstagramProfile::name)
////                .collect(Collectors.groupingBy(
////                        Function.identity(),
////                        Collectors.counting()
////                ));
////
////        LinkedHashMap<String, Long> rating = map.entrySet().stream()
////                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
////                .collect(Collectors.toMap(
////                        Map.Entry::getKey,
////                        Map.Entry::getValue,
////                        (u, v) -> v,
////                        LinkedHashMap::new));
////
////        for (Map.Entry<String, Long> entry : rating.entrySet()) {
////            System.out.println(entry.getKey() + " - " + entry.getValue());
////        }
//    }
}
