package com.kiselev.enemy.instagram.configuration;

import com.kiselev.enemy.instagram.InstagramService;
import com.kiselev.instagram.Instagram;
import com.kiselev.instagram.analytics.InstagramAnalytic;
import com.kiselev.instagram.analytics.comparator.InstagramComparator;
import com.kiselev.instagram.analytics.reader.InstagramReader;
import com.kiselev.instagram.analytics.reader.ReaderMode;
import com.kiselev.instagram.analytics.writer.InstagramWriter;
import com.kiselev.instagram.api.InstagramAPI;
import com.kiselev.instagram.api.client.InstagramClient;
import com.kiselev.instagram.api.credentials.InstagramCredentials;
import com.kiselev.instagram.api.internal.InstagramInternalAPI;
import com.kiselev.instagram.model.mapper.InstagramMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InstagramConfiguration {

    @Value("${com.kiselev.enemy.username}")
    private String username;

    @Value("${com.kiselev.enemy.password}")
    private String password;

    @Value("#{'${com.kiselev.enemy.reader.mode}'}")
    private ReaderMode readerMode;

    @Bean
    public InstagramCredentials instagramCredentials() {
        return InstagramCredentials.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public InstagramClient instagramClient(InstagramCredentials instagramCredentials) {
        return new InstagramClient(
                instagramCredentials
        );
    }

    @Bean
    public InstagramInternalAPI instagramInternalAPI(InstagramClient instagramClient) {
        return new InstagramInternalAPI(
                instagramClient
        );
    }

    @Bean
    public InstagramAPI instagramAPI(InstagramInternalAPI instagramInternalAPI) {
        InstagramAPI instagramAPI = new InstagramAPI(
                instagramInternalAPI
        );
        // Authentication
        instagramAPI.authenticate();
        return instagramAPI;
    }

    @Bean
    public InstagramMapper instagramMapper() {
        return new InstagramMapper(
                readerMode
        );
    }

    @Bean
    public InstagramReader instagramReader(InstagramAPI instagramAPI,
                                           InstagramMapper instagramMapper) {
        return new InstagramReader(
                instagramAPI,
                instagramMapper
        );
    }

    @Bean
    public InstagramWriter instagramWriter() {
        return new InstagramWriter();
    }

    @Bean
    public InstagramComparator instagramComparator() {
        return new InstagramComparator();
    }

    @Bean
    public InstagramAnalytic instagramAnalytic(InstagramReader instagramReader,
                                               InstagramWriter instagramWriter,
                                               InstagramComparator instagramComparator) {
        return new InstagramAnalytic(
                instagramReader,
                instagramWriter,
                instagramComparator
        );
    }

    @Bean
    public Instagram instagram(InstagramAPI instagramAPI,
                               InstagramAnalytic instagramAnalytic) {
        return new Instagram(
                instagramAPI,
                instagramAnalytic
        );
    }

    @Bean
    public InstagramService instagramService(Instagram instagram) {
        return new InstagramService(
                instagram
        );
    }
}
