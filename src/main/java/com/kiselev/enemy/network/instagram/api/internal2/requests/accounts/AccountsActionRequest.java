package com.kiselev.enemy.network.instagram.api.internal2.requests.accounts;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.AccountsUserResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsActionRequest extends IGPostRequest<AccountsUserResponse> {
    @NonNull
    private AccountsAction action;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return "accounts/" + action.name().toLowerCase() + "/";
    }

    @Override
    public Class<AccountsUserResponse> getResponseType() {
        return AccountsUserResponse.class;
    }

    public static enum AccountsAction {
        SET_PRIVATE, SET_PUBLIC, REMOVE_PROFILE_PICTURE;
    }
}
