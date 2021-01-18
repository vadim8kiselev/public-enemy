package com.kiselev.enemy.network.instagram.api.internal2.responses.users;

import com.kiselev.enemy.network.instagram.api.internal2.models.friendships.Friendship;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsersSearchResponse extends IGResponse {

    private int num_results;
    private List<User> users;
    private boolean has_more;
    private String rank_token;
    private String page_token;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class User extends Profile {
        Friendship friendship_status;
        String social_context;
        String search_social_context;
        int mutual_followers_count;
        int latest_reel_media;
    }

}
