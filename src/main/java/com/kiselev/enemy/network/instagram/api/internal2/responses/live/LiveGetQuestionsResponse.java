package com.kiselev.enemy.network.instagram.api.internal2.responses.live;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class LiveGetQuestionsResponse extends IGResponse {
    private List<LiveQuestions> questions;

    @Data
    public static class LiveQuestions {
        private String text;
        private long qid;
        private String source;
        private Profile user;
        private long timestamp;
    }
}