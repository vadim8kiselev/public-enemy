package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramSearchUsersResult extends StatusResult {
    private List<InstagramSearchUsersResultUser> users;
    private boolean has_more;
    private int num_results;


}
