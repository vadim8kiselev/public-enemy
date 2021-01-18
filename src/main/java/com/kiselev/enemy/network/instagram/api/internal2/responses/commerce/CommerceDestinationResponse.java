package com.kiselev.enemy.network.instagram.api.internal2.responses.commerce;

import com.kiselev.enemy.network.instagram.api.internal2.models.discover.SectionalMediaGridItem;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;
import lombok.Data;

import java.util.List;

@Data
public class CommerceDestinationResponse extends IGPaginatedResponse {
    private List<SectionalMediaGridItem> sectional_items;
    private String rank_token;
    private String next_max_id;
    private boolean more_available;
}
