package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramGetUserFollowersResult extends StatusResult {

    public boolean big_list;
    public String next_max_id;
    public int page_size;

    public List<InstagramUserSummary> users;

}