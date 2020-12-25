package com.kiselev.enemy.utils.flow.model;

import com.kiselev.enemy.utils.flow.message.Message;

public interface Info extends Id {

    SocialNetwork type();

    default String header() {
        return String.format(
                Message.HEADER_TEMPLATE,
                type().name(),
                name()
        );
    }
}
