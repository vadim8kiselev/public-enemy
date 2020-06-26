package com.kiselev.enemy.instagram;

import com.kiselev.instagram.Instagram;
import com.kiselev.instagram.analytics.InstagramAnalytic;
import com.kiselev.instagram.model.InstagramProfile;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InstagramService {

    private final Instagram instagram;

    public InstagramProfile read(String username) {
        InstagramAnalytic analytic = instagram.analytic();
        return analytic.read(username);
    }

    public String write(InstagramProfile instagramProfile) {
        InstagramAnalytic analytic = instagram.analytic();
        return analytic.write(instagramProfile);
    }

    public List<String> compare(InstagramProfile actualInstagramProfile,
                                InstagramProfile latestInstagramProfile) {
        InstagramAnalytic analytic = instagram.analytic();
        return analytic.compare(actualInstagramProfile, latestInstagramProfile);
    }
}
