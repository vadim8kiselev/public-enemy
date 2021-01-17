package com.kiselev.enemy.network.instagram.api.internal2.exceptions;

import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGResponseException.IGFailedResponse;

public interface ExceptionallyHandler {

    ExceptionallyHandler WRAPPED_TO_IGRESPONSE = new ExceptionallyHandler() {
        @Override
        public <T> T handle(Throwable throwable, Class<T> type) {
            return IGFailedResponse.of(throwable.getCause(), type);
        }
    };

    <T> T handle(Throwable throwable, Class<T> type);
}
