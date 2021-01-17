package com.kiselev.enemy.network.instagram.api.internal2.models.media.reel;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import lombok.Data;

import java.util.List;

@Data
public class ResponderInfo extends IGBaseModel {
    private Long question_id;
    private String question;
    private String question_type;
    private List<Responder> responders;
    private String max_id;
    private boolean more_available;

    @Data
    public static class Responder {
        private Profile user;
        private String response;
        private String id;
        private Long ts;
    }
}
