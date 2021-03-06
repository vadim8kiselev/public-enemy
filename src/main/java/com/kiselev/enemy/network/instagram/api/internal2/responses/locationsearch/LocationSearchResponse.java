package com.kiselev.enemy.network.instagram.api.internal2.responses.locationsearch;

import com.kiselev.enemy.network.instagram.api.internal2.models.location.Location.Venue;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

import java.util.List;

@Data
public class LocationSearchResponse extends IGResponse {
    private List<Venue> venues;
    private String request_id;
    private String rank_token;
}
