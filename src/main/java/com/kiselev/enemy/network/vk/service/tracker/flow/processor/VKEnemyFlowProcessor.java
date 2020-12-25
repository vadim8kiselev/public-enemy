package com.kiselev.enemy.network.vk.service.tracker.flow.processor;

import com.google.common.collect.Lists;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.utils.FlowUtils;
import com.kiselev.enemy.utils.flow.annotation.EnemyValue;
import com.kiselev.enemy.utils.flow.annotation.EnemyValues;
import com.kiselev.enemy.network.vk.service.tracker.flow.VKFlowProcessor;
import com.kiselev.enemy.network.vk.model.VKProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class VKEnemyFlowProcessor implements VKFlowProcessor {

    @Override
    @SuppressWarnings("unchecked")
    public List<Message<VKProfile>> process(VKProfile actual, VKProfile latest) {
        Class<VKProfile> clazz = VKProfile.class;
        List<Message<VKProfile>> messages = Lists.newArrayList();

        for (Field field : clazz.getDeclaredFields()) {
            EnemyValue enemyValue = field.getAnnotation(EnemyValue.class);
            EnemyValues enemyValues = field.getAnnotation(EnemyValues.class);

            String fieldName = field.getName();
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, fieldName);
            Method method = pd.getReadMethod();
            ReflectionUtils.makeAccessible(method);

            if (enemyValue != null) {
                messages.add(
                        FlowUtils.attribute(
                                profile -> attributeFunction(method, profile),
                                actual,
                                latest,
                                enemyValue.message()));
            } else if (enemyValues != null) {
                messages.addAll(
                        FlowUtils.attributes(
                                profile -> attributesFunction(method, profile),
                                actual,
                                latest,
                                enemyValues.newMessage()));
                messages.addAll(
                        FlowUtils.attributes(
                                profile -> attributesFunction(method, profile),
                                latest,
                                actual,
                                enemyValues.deleteMessage()));
            }
        }
        return messages;
    }

    private String attributeFunction(Method method, VKProfile profile) {
        return (String) ReflectionUtils.invokeMethod(method, profile);
    }

    @SuppressWarnings("rawtypes")
    private List attributesFunction(Method method, VKProfile profile) {
        return (List) ReflectionUtils.invokeMethod(method, profile);
    }
}