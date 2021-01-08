package com.kiselev.enemy.utils.flow.model;

import com.kiselev.enemy.utils.flow.message.EnemyMessage;

public interface Info extends Id {

    SocialNetwork type();

    default String header() {
        return String.format(
                EnemyMessage.HEADER_TEMPLATE,
                type().name(),
                name()
        );
    }
}
