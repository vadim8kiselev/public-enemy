package com.kiselev.enemy.network.instagram.api.internal2.requests.linkedaccounts;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

public class LinkedAccountsGetLinkageStatusRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "linked_accounts/get_linkage_status/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
