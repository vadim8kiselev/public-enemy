package com.kiselev.enemy.network.instagram.api.credentials;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class InstagramCredentials {

    @Value("${com.kiselev.enemy.instagram.username}")
    private String username;

    @Value("${com.kiselev.enemy.instagram.password}")
    private String password;
}
