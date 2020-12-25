package com.kiselev.enemy.network.instagram.api.internal2.responses.discover;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.discover.Cluster;
import com.kiselev.enemy.network.instagram.api.internal2.models.discover.SectionalItem;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

import lombok.Data;

@Data
public class DiscoverTopicalExploreResponse extends IGPaginatedResponse {
    private List<SectionalItem> sectional_items;
    private String rank_token;
    private List<Cluster> clusters;
    private String next_max_id;
    private boolean more_available;
}
