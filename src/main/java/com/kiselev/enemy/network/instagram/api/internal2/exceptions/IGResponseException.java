package com.kiselev.enemy.network.instagram.api.internal2.exceptions;

import java.io.IOException;

import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class IGResponseException extends IOException {
    @NonNull
    private IGResponse response;

    public IGResponseException(IGResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    @Data
    public static class IGFailedResponse extends IGResponse {
        private final String status = "fail";
        private final String message;

        public static IGResponse of(Throwable throwable) {
            if (throwable instanceof IGResponseException)
                return ((IGResponseException) throwable).getResponse();
            return new IGFailedResponse(throwable.toString());
        }

        public static <T> T of(Throwable throwable, Class<T> clazz) {
            return IGUtils.convertToView(of(throwable), clazz);
        }
    }

}
