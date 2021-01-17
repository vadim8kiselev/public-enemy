package com.kiselev.enemy.network.instagram.api.internal2.models.media.reel;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import lombok.Data;

import java.util.List;

@Data
public class VoterInfo extends IGBaseModel {
    private Long poll_id;
    private List<Voter> voters;
    private String max_id;
    private boolean more_available;

    @Data
    public static class Voter {
        private Profile user;
        private int vote;
        private Long ts;
    }
}
