package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramSearchTagsResult extends StatusResult {
    private List<InstagramSearchTagsResultTag> results;
    private boolean has_more;
    private int num_results;

}
