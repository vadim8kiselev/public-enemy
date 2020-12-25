package com.kiselev.enemy.network.instagram.api.internal2.responses.live;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class LiveBroadcastLikeResponse extends IGResponse {
    private String likes;
    private String burst_likes;

    @Data
    public static class LiveBroadcastGetLikeCountResponse extends LiveBroadcastLikeResponse {
        private long like_ts;
        private List<Liker> likers;

        @Data
        public static class Liker {
            private String user_id;
            private String profile_pic_url;
            private int count;
        }
    }
}
