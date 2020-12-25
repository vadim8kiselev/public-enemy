package com.kiselev.enemy.network.instagram.api.internal2.responses.igtv;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.igtv.Channel;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class IgtvSearchResponse extends IGResponse {
    private List<IgtvSearchResult> results;
    private int num_results;
    private boolean has_more;
    private String rank_token;

    @Data
    public static class IgtvSearchResult {
        private String type;
        private User user;
        private Channel channel;
    }
}
