package com.kiselev.enemy.network.instagram.service.tracker.flow.processor;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.service.tracker.flow.InstagramFlowProcessor;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstagramFollowersFlowProcessor implements InstagramFlowProcessor {

    @Override
    public List<EnemyMessage<InstagramProfile>> process(InstagramProfile actual, InstagramProfile latest) {
        return Lists.newArrayList();
    }
}
