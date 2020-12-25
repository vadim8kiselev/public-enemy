package com.kiselev.enemy.network.instagram.api.internal2.exceptions;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGResponseException;
import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.LoginResponse;

import lombok.Getter;

@Getter
public class IGLoginException extends IGResponseException {
    private final IGClient client;
    private final LoginResponse loginResponse;

    public IGLoginException(IGClient client, LoginResponse body) {
        super(body);
        this.client = client;
        this.loginResponse = body;
    }

}
