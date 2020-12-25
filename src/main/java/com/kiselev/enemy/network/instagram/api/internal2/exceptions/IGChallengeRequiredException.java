package com.kiselev.enemy.network.instagram.api.internal2.exceptions;

import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.LoginResponse;
import lombok.Data;

@Data
public class IGChallengeRequiredException extends RuntimeException {

    private LoginResponse response;

    public IGChallengeRequiredException(LoginResponse response) {
        this.response = response;
    }
}
