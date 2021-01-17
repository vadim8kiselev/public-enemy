package com.kiselev.enemy.network.instagram.api.internal2.requests.commerce;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.commerce.CommerceDestinationResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CommerceDestinationRequest extends IGGetRequest<CommerceDestinationResponse>
        implements IGPaginatedRequest<CommerceDestinationResponse> {
    @Setter
    private String max_id = "0";

    @Override
    public String path() {
        return "commerce/destination/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("max_id", max_id, "cluster_id", "shopping");
    }

    @Override
    public Class<CommerceDestinationResponse> getResponseType() {
        return CommerceDestinationResponse.class;
    }

}
