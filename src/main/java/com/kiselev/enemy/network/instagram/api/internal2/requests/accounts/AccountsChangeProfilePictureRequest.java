package com.kiselev.enemy.network.instagram.api.internal2.requests.accounts;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.AccountsUserResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsChangeProfilePictureRequest extends IGPostRequest<AccountsUserResponse> {
    @NonNull
    private String _upload_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String upload_id = _upload_id;
        };
    }

    @Override
    public String path() {
        return "accounts/change_profile_picture/";
    }

    @Override
    public Class<AccountsUserResponse> getResponseType() {
        return AccountsUserResponse.class;
    }

}
