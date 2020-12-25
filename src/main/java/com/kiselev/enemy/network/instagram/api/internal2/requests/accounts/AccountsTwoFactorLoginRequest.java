package com.kiselev.enemy.network.instagram.api.internal2.requests.accounts;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsLoginRequest.LoginPayload;
import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.LoginResponse;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsTwoFactorLoginRequest extends IGPostRequest<LoginResponse> {
    @NonNull
    private String username, password, code, identifier;

    @Override
    public IGPayload getPayload(IGClient client) {
        return new LoginPayload(username, password) {
            @Getter
            private final String verification_code = code;
            @Getter
            private final String two_factor_identifier = identifier;
        };
    }

    @Override
    public String path() {
        return "accounts/two_factor_login/";
    }

    @Override
    public Class<LoginResponse> getResponseType() {
        return LoginResponse.class;
    }

}
